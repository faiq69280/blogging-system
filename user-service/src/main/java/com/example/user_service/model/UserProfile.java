package com.example.user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table
@Builder
@AllArgsConstructor
public class UserProfile extends BaseEntity {
    @Column
    private String email;

    @Column
    private String avatarUrl;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    User user;

    public UserProfile() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
