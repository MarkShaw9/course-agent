package com.elec5620.course_agent_backend.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {


    private String secret = "MY5crEt9JhixfX9Rhc+mJ2LpzE757fjQsdklvY78mfsdjklw="; // 从配置文件中读取密钥



    // 获取签名密钥
    private SecretKey getSigningKey() {
        // 将字符串密钥转换为 SecretKey
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 生成 JWT Token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())  // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 设置过期时间，7天
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // 使用HS256算法和密钥签名
                .compact();
    }

    // 验证 Token
    public boolean validateToken(String token, String username) {
        String tokenUsername = extractUsername(token);
        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }

    // 提取 Token 中的用户名
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 判断 Token 是否过期
    public boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    @PostConstruct
    public void checkSecret() {
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException("JWT secret is not set or empty");
        }
        System.out.println("JWT secret successfully injected: " + secret);
    }
}