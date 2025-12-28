package com.example.user_service.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthenticationContext implements Authentication {
    private UserDetailsWithId principal;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isAuthenticated;

    public UserAuthenticationContext(UserDetailsWithId principal, Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
        this.principal = principal;
        this.setAuthenticated(true);
    }

    public UserDetailsWithId getUserDetailsWithId() {
        return this.principal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable Object getCredentials() {
        return null;
    }

    @Override
    public @Nullable Object getDetails() {
        return null;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return this.principal;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return principal.getUsername();
    }
}
