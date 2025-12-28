package com.example.user_service.dto;

import com.example.user_service.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record UserDTO(@NotNull String userName, @NotNull @Length(min=12) String password, @Email String email, @NotNull List<Role.RoleName> roles, String avatarUrl) {
}
