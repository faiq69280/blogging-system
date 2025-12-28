package com.example.article_api.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsWithId extends UserDetails {
    public String getUserId();
}
