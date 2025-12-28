package com.example.user_service.service;

import com.example.user_service.dto.AuthenticatedResponseDTO;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.dto.UserWithProfileDTO;
import com.example.user_service.exception.ConflictException;
import com.example.user_service.model.Role;
import com.example.user_service.model.User;
import com.example.user_service.model.UserProfile;
import com.example.user_service.repository.RoleRepository;

import com.example.user_service.repository.UserRepository;
import com.example.user_service.security.JwtUtils;
import com.example.user_service.security.UserDetailsWithId;
import com.example.user_service.utils.CommonObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Transactional
    public UserWithProfileDTO create(User user, List<Role.RoleName> roleNames) throws ConflictException {
        Optional<User> userFound = userRepository.findByName(user.getName());
        CommonObjectUtils.requireAbsent(userFound, "user already exists");

        List<Role> roles = roleRepository.findByNameIn(roleNames);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User userSaved = Optional.of(userRepository.save(user)).orElseThrow();
        return convertToDTO(userSaved, user.getUserProfile());
    }

    public AuthenticatedResponseDTO verifyUserCredentials(UserDTO userDTO) {
        Objects.requireNonNull(userDTO.userName(), "Empty/Null userName provided!");
        Objects.requireNonNull(userDTO.password(), "Empty password provided!");

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDTO.userName(), userDTO.password());
        Authentication authenticatedTokenContext = authenticationManager.authenticate(authentication);

        //DAOAuthenticationProvider is used by default for UserNamePasswordAuthentication so authenticatedTokenContext will have UserDetails as its principal, as it will use our UserService.
        //Safe to downcast as principal is literally what our UserDetailsService returns through loadByUserName method
        UserDetailsWithId userDetailsWithId = (UserDetailsWithId) authenticatedTokenContext.getPrincipal();
        String jwtToken = jwtUtils.generateToken(userDetailsWithId);

        return AuthenticatedResponseDTO.builder()
                .jwt(jwtToken)
                .userName(userDTO.userName())
                .build();
    }

    public UserWithProfileDTO convertToDTO(User user, UserProfile userProfile) {
        UserWithProfileDTO.UserWithProfileDTOBuilder userWithProfileDTOBuilder = UserWithProfileDTO.builder().userId(user.getId()).userName(user.getName());
        return userProfile != null ? userWithProfileDTOBuilder.build() : userWithProfileDTOBuilder.avatarUrl(userProfile.getAvatarUrl()).email(userProfile.getEmail()).build();
    }
}
