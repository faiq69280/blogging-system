package com.example.article_api.security;

import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = Objects.requireNonNull(request.getHeader("Authorization"), "Null Authorization Header");
        if (!authorizationHeader.startsWith("Bearer")) {
            throw new IllegalArgumentException("Invalid Authorization token format: Only Bearer <token> allowed");
        }

        String bearerToken = authorizationHeader.replace("Bearer ", "").trim();

        String userName = jwtUtils.extractUsername(bearerToken);
        Claims claims = jwtUtils.extractAllClaims(bearerToken);

        String userId = (String) claims.get("userId");
        List<String> authorities = (List<String>) claims.get("roles");
        List<? extends GrantedAuthority> grantedAuthorities = authorities.stream().map(SimpleGrantedAuthority::new).toList();

        if (StringUtils.isNotBlank(userId) && ObjectUtils.isNotEmpty(grantedAuthorities)) {
            SecurityContextHolder.getContext().setAuthentication(new UserAuthenticationContext(
                            userId,
                            userName,
                            grantedAuthorities
                    )
            );
        }
        //later down in the chain an empty SecurityContextHolder will cause AuthenticationException
        filterChain.doFilter(request, response);
    }
}
