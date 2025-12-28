package com.example.user_service.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsWithId extends UserDetails {
    public String getUserId();
}
