package com.example.article_api.security;

import com.example.article_api.security.cryptography.RSAPublicKeyGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;


@Service
public class JwtUtils {
    @Value("${security.jwt.public-key}")
    private Resource publicKeyResource;

    @Value("${security.jwt.expiration-ms:3600000}")
    private long expirationMs;

   // Only non-null in auth-service
    private Key publicKey;

    @PostConstruct
    void initKeys() {
        this.publicKey = new RSAPublicKeyGenerator().loadKeyFromResource(publicKeyResource);
    }

     public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public Claims extractAllClaims(String token) {
        // NOTE: we only need publicKey here → works in all services
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}