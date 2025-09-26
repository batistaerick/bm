package com.budgetmanager.bm.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.access-expiration}")
    private int accessExpiration;
    @Value("${jwt.secret}")
    private String secret;

    public String generateAccessToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessExpiration);

        return JWT
            .create()
            .withSubject(username)
            .withIssuedAt(now)
            .withExpiresAt(expiration)
            .sign(Algorithm.HMAC256(secret.getBytes()));
    }

    public DecodedJWT validateAccessToken(String token) {
        return JWT
            .require(Algorithm.HMAC256(secret.getBytes()))
            .build()
            .verify(token);
    }
}
