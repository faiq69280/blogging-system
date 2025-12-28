package com.example.user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table
@AllArgsConstructor
public class Role extends BaseEntity {
    @Column
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    List<User> users;

    public Role() {}

    public RoleName getName() { return name; }
    public void setName(RoleName name) { this.name = name; }

    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }

    public enum RoleName {
        ADMIN,
        BLOGGER
    }
}
