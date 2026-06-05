package com.flow.payflow.config.component;

import com.flow.payflow.annotation.Idempotence;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class IdempotenceAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Around("@annotation(idempotence)")
    public Object managmentIdempotence(ProceedingJoinPoint joinPoint, Idempotence idempotence) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();

        String keyIdempotence = request.getHeader("X-Idempotency-Key");
        if (keyIdempotence == null || keyIdempotence.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O cabeçalho X-Idempotency-Key é obrigatório.");
        }

        String keyRedis = "payment:idempotence:"+keyIdempotence;
        long timeout = idempotence.timeout();

        Boolean success = redisTemplate.opsForValue().setIfAbsent(keyRedis, "PROCCESS", timeout, TimeUnit.MINUTES);

        if (success == null || !success) {
            String value = redisTemplate.opsForValue().get(keyRedis);
            if ("PROCCESS".equals(value)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "A transação está sendo processada.");
            }
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Transação duplicada detectada.");
        }

        try {
            Object result = joinPoint.proceed();
            redisTemplate.opsForValue().set(keyRedis, "SUCCESS", timeout, TimeUnit.MINUTES);
            return result;
        } catch (Throwable e) {
            redisTemplate.delete(keyRedis);
            throw e;
        }

    }

}
