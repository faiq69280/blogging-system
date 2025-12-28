package com.example.user_service.controller;

import com.example.user_service.dto.UserWithProfileDTO;
import com.example.user_service.exception.NotFoundException;
import com.example.user_service.security.UserAuthenticationContext;
import com.example.user_service.security.UserDetailsWithId;
import com.example.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("getUserInfo")
    public ResponseEntity<UserWithProfileDTO> getUserInfo() throws NotFoundException {
       UserAuthenticationContext userAuthCtx = (UserAuthenticationContext) SecurityContextHolder.getContext().getAuthentication();
       String userId = Objects.requireNonNull(userAuthCtx.getUserDetailsWithId(), "No userDetails found in context").getUserId();
       return ResponseEntity.ok(userService.getUserWithProfile(userId));
    }

    @GetMapping(value = "getUserInfo", headers = {
            "X-Internal-Secret",
            "X-Service-Id"
    })
    public ResponseEntity<UserWithProfileDTO> getUserInfoInternal(@RequestParam(value = "userId") String userId) throws NotFoundException {
        return ResponseEntity.ok(userService.getUserWithProfile(userId));
    }
}
