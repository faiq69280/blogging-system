package com.example.user_service.security;

import com.example.user_service.security.cryptography.KeyGenerator;
import com.example.user_service.security.cryptography.RSAPrivateKeyGenerator;
import com.example.user_service.security.cryptography.RSAPublicKeyGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtils {

    @Value("${security.jwt.private-key}")
    private Resource privateKeyResource;

    @Value("${security.jwt.public-key}")
    private Resource publicKeyResource;

    @Value("${security.jwt.expiration-ms:3600000}")
    private long expirationMs;

    private Key privateKey;   // Only non-null in auth-service
    private Key publicKey;

    @PostConstruct
    void initKeys() {
        this.privateKey =  new RSAPrivateKeyGenerator().loadKeyFromResource(privateKeyResource);
        this.publicKey = new RSAPublicKeyGenerator().loadKeyFromResource(publicKeyResource);
    }

    public String generateToken(UserDetailsWithId user) {
        if (privateKey == null) {
            throw new IllegalStateException("No private key configured for signing");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        claims.put("userId", user.getUserId());

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(privateKey, SignatureAlgorithm.RS256)  // <-- key change
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token,UserDetailsWithId user) {
        final String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
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