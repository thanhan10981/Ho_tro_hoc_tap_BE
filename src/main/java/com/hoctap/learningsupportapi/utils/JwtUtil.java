package com.hoctap.learningsupportapi.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "MY_SUPER_STRONG_SECRET_KEY_2025_ABCXYZ123456!!";
    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 1 ngày

    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public static String generateToken(Integer userId, String email) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Integer getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Integer.parseInt(claims.getSubject());
    }
}
