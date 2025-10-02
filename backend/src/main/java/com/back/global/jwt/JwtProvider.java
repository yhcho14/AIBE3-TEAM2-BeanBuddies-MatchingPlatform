package com.back.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtProvider {

    private final String accessSecret;
    private final long accessExpireMillis;

    private final String refreshSecret;
    private final long refreshExpireMillis;

    public JwtProvider(
            @Value("${custom.jwt.accessToken.secretKey}") String accessSecret,
            @Value("${custom.jwt.accessToken.expireSeconds}") long accessExpireSeconds,
            @Value("${custom.jwt.refreshToken.secretKey}") String refreshSecret,
            @Value("${custom.jwt.refreshToken.expireSeconds}") long refreshExpireSeconds
    ) {
        this.accessSecret = accessSecret;
        this.accessExpireMillis = accessExpireSeconds * 1000L;
        this.refreshSecret = refreshSecret;
        this.refreshExpireMillis = refreshExpireSeconds * 1000L;
    }

    // 토큰 생성
    public String generateAccessToken(Map<String, Object> claims) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + accessExpireMillis);

        Key key = Keys.hmacShaKeyFor(accessSecret.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    // 토큰 생성
    public String generateRefreshToken(Map<String, Object> claims) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + refreshExpireMillis);

        Key key = Keys.hmacShaKeyFor(refreshSecret.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }


    // 토큰 검증
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }

    // claim 추출
//    public Claims getClaims(String token) {
//        try {
//            return Jwts.parserBuilder()
//                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (Exception e) {
//            return null;
//        }
//    }
}