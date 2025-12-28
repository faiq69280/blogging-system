package com.example.user_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class InternalAuthFilter extends OncePerRequestFilter {

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader("X-Internal-Secret") != null && request.getHeader("X-Service-Id") != null) {
            SecurityContextHolder.getContext().setAuthentication(new
                    InternalAuthenticationContext(request.getHeader("X-Internal-Secret"),
                    request.getHeader("X-Service-Id"),
                    List.of(new SimpleGrantedAuthority(InternalAuthenticationContext.InternalRole.INTERNAL_SERVICE.toString()))));
        }

        filterChain.doFilter(request, response);
    }
}
