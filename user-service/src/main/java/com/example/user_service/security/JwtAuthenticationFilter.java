package com.example.user_service.security;

import com.example.user_service.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            String authorizationHeader = Objects.requireNonNull(request.getHeader("Authorization"), "Null Authorization Header");
            if (!authorizationHeader.startsWith("Bearer")) {
                throw new IllegalArgumentException("Invalid Authorization token format: Only Bearer <token> allowed");
            }

            String bearerToken = authorizationHeader.replace("Bearer ", "").trim();

            String userName = jwtUtils.extractUsername(bearerToken);
            UserDetailsWithId userDetailsWithId = userService.loadUserByUsername(userName);

            if (userDetailsWithId != null && jwtUtils.isTokenValid(bearerToken, userDetailsWithId)) {
                SecurityContextHolder.getContext().setAuthentication(new UserAuthenticationContext(
                                userDetailsWithId,
                                userDetailsWithId.getAuthorities()
                        )
                );
            }
        }
        //later down in the chain an empty SecurityContextHolder will cause AuthenticationException
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth/");
    }
}
