package com.example.user_service.repository;

import com.example.user_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String> {
    public List<Role> findByNameIn(List<Role.RoleName> names);
}
