package com.game.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "fengshen-game-secret-key-2024-fengshen-game-secret-key-2024";
    private static final long EXPIRATION = 86400000; // 24小时
    
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    
    public static String generateToken(Long userId, String username) {
        return Jwts.builder()
            .subject(userId + ":" + username)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
            .signWith(KEY)
            .compact();
    }
    
    public static String getUsernameFromToken(String token) {
        return Jwts.parser().verifyWith(KEY).build()
            .parseSignedClaims(token).getPayload().getSubject().split(":")[1];
    }
    
    public static Long getUserIdFromToken(String token) {
        String subject = Jwts.parser().verifyWith(KEY).build()
            .parseSignedClaims(token).getPayload().getSubject();
        return Long.parseLong(subject.split(":")[0]);
    }
    
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
