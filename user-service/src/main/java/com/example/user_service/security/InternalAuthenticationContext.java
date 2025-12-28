package com.example.user_service.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class InternalAuthenticationContext implements Authentication {
    private String internalSecret;
    private String internalAuthPrincipal;
    private List<GrantedAuthority> grantedAuthorities;
    private boolean isAuthenticated = false;

    public InternalAuthenticationContext(String internalSecret, String internalAuthPrincipal,
                                         List<GrantedAuthority> authorities) {
        this.internalSecret = internalSecret;
        this.internalAuthPrincipal = internalAuthPrincipal;
        this.grantedAuthorities = authorities;
        this.isAuthenticated = true;
    }

    /**
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    /**
     * @return
     */
    @Override
    public @Nullable Object getCredentials() {
        return internalSecret;
    }

    /**
     * @return
     */
    @Override
    public @Nullable Object getDetails() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public @Nullable Object getPrincipal() {
        return internalAuthPrincipal;
    }

    /**
     * @return
     */
    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    /**
     * @param isAuthenticated
     * @throws IllegalArgumentException
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    /**
     * @return
     */
    @Override
    public String getName() {
        return internalAuthPrincipal;
    }

    public enum InternalRole {
        INTERNAL_SERVICE
    }
}
