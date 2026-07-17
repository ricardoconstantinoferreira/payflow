package com.flow.payflow.config.component;

import com.flow.payflow.annotation.Idempotence;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdempotenceAspectTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private IdempotenceAspect aspect;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @BeforeEach
    void setup() {
        // wire ValueOperations
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @AfterEach
    void cleanup() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void managmentIdempotence_whenSetIfAbsentTrue_proceedsAndSetsSuccess() throws Throwable {
        // prepare request with header
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Idempotency-Key")).thenReturn("abc123");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        String key = "payment:idempotence:abc123";
        long timeout = 1L;

        when(valueOperations.setIfAbsent(key, "PROCCESS", timeout, TimeUnit.MINUTES)).thenReturn(Boolean.TRUE);
        when(joinPoint.proceed()).thenReturn("ok");

        Object result = aspect.managmentIdempotence(joinPoint, new Idempotence() {
            @Override
            public long timeout() { return timeout; }
            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() { return Idempotence.class; }
        });

        assertEquals("ok", result);
        verify(valueOperations).set(key, "SUCCESS", timeout, TimeUnit.MINUTES);
    }

    @Test
    void managmentIdempotence_whenAlreadyProcessing_throwsConflict() throws Throwable {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Idempotency-Key")).thenReturn("abc123");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        String key = "payment:idempotence:abc123";
        long timeout = 1L;

        when(valueOperations.setIfAbsent(key, "PROCCESS", timeout, TimeUnit.MINUTES)).thenReturn(Boolean.FALSE);
        when(valueOperations.get(key)).thenReturn("PROCCESS");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                aspect.managmentIdempotence(joinPoint, new Idempotence() {
                    @Override public long timeout() { return timeout; }
                    @Override public Class<? extends java.lang.annotation.Annotation> annotationType() { return Idempotence.class; }
                })
        );

        assertEquals(409, ex.getStatusCode().value());
        String reason = ex.getReason();
        assertNotNull(reason);
        assertTrue(reason.contains("processo") || reason.toLowerCase().contains("process"));
    }

    @Test
    void managmentIdempotence_whenDuplicateDetected_throwsConflict() throws Throwable {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Idempotency-Key")).thenReturn("abc123");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        String key = "payment:idempotence:abc123";
        long timeout = 1L;

        when(valueOperations.setIfAbsent(key, "PROCCESS", timeout, TimeUnit.MINUTES)).thenReturn(Boolean.FALSE);
        when(valueOperations.get(key)).thenReturn("SUCCESS");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                aspect.managmentIdempotence(joinPoint, new Idempotence() {
                    @Override public long timeout() { return timeout; }
                    @Override public Class<? extends java.lang.annotation.Annotation> annotationType() { return Idempotence.class; }
                })
        );

        assertEquals(409, ex.getStatusCode().value());
        String reason = ex.getReason();
        assertNotNull(reason);
        assertTrue(reason.toLowerCase().contains("duplic") || reason.toLowerCase().contains("duplicate"));
    }

    @Test
    void managmentIdempotence_whenProceedThrows_deletesKeyAndRethrows() throws Throwable {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Idempotency-Key")).thenReturn("abc123");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        String key = "payment:idempotence:abc123";
        long timeout = 1L;

        when(valueOperations.setIfAbsent(key, "PROCCESS", timeout, TimeUnit.MINUTES)).thenReturn(Boolean.TRUE);
        when(joinPoint.proceed()).thenThrow(new RuntimeException("boom"));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                aspect.managmentIdempotence(joinPoint, new Idempotence() {
                    @Override public long timeout() { return timeout; }
                    @Override public Class<? extends java.lang.annotation.Annotation> annotationType() { return Idempotence.class; }
                })
        );

        assertEquals("boom", ex.getMessage());
        verify(redisTemplate).delete(key);
    }

}
