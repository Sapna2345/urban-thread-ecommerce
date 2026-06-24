package com.Sapna.Ecomm.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "this-is-a-very-secure-secret-key-for-ecomm-project-12345";
    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }
     public String generateToken(Long userId, String email){
        return Jwts.builder()
                 .subject(email)
                 .claim("userId", userId)
                 .issuedAt(new Date())
                 .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                 .signWith(getSigningKey())
                 .compact();
     }
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return extractClaims(token).get("userId", Long.class);
    }

    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception error) {
            return false;
        }
    }
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
