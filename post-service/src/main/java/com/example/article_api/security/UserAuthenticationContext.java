package com.example.article_api.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthenticationContext implements Authentication {
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isAuthenticated;
    private String userId;
    private String userName;


    public UserAuthenticationContext(String userId, String userName, Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
        this.userId = userId;
        this.userName = userName;
        isAuthenticated = true;
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
        return this.userId;
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
        return this.userName;
    }
}
