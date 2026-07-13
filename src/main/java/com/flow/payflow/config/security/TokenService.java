package com.flow.payflow.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.flow.payflow.entity.Store;
import com.flow.payflow.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@Service
public class TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);
    @Value("${api.security.token.secret}")
    private String secret;

    @Autowired
    private StoreService storeService;

    public String generateToken(Store store) {
        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);
            var username = store.getEmail();

            String token = JWT.create()
                    .withIssuer("jwt_auto_automoveis")
                    .withSubject(username)
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);

            if (!token.isEmpty()) {
                storeService.saveToken(store, token);
                log.info("Token gerado com sucesso.");
            }

            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error in the generating token", e);
        }
    }

    public String validateToken(String token) {
        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);
            var email = JWT.require(algorithm)
                    .withIssuer("jwt_auto_automoveis")
                    .build()
                    .verify(token).getSubject();

            if (!email.isEmpty()) {
                Store customer = storeService.getStoreByEmail(email);

                if (!customer.getToken().equals(token)) {
                    log.error("Token não validado.");
                    return "";
                }
            }

            log.info("Token validado com sucesso.");
            return email;


        } catch (JWTVerificationException e) {
            log.error("Token não validado.");
            return "";
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
