package com.budgetmanager.bm.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Value("${jwt.secret}")
    private String secret;

    public String generateAccessToken(String username, List<String> roles) {
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(accessExpiration);

        return JWT.create()
            .withSubject(username)
            .withClaim("roles", roles)
            .withIssuedAt(now)
            .withExpiresAt(expiration)
            .sign(Algorithm.HMAC256(secret.getBytes()));
    }

    public DecodedJWT validateAccessToken(String token) {
        return JWT.require(Algorithm.HMAC256(secret.getBytes()))
            .build()
            .verify(token);
    }
}
