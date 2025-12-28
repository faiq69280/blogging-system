package com.example.user_service.service;

import com.example.user_service.dto.UserWithProfileDTO;
import com.example.user_service.exception.NotFoundException;
import com.example.user_service.model.User;
import com.example.user_service.model.UserProfile;
import com.example.user_service.repository.UserProfileRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.security.UserDetailsWithId;
import com.example.user_service.utils.CommonObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @Transactional
    public UserWithProfileDTO getUserWithProfile(String userId) throws NotFoundException {
        Optional<User> userFound = userRepository.findById(userId);
        User validUser = CommonObjectUtils.requirePresent(userFound, "user not found");
        UserProfile profileFetched = userProfileRepository.findByUserId(validUser.getId()).orElse(null);
        return convertToDTO(validUser, profileFetched);
    }

    @Override
    public UserDetailsWithId loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return CommonObjectUtils.requirePresent(userRepository.findByName(username), "Couldn't find user");
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    private UserWithProfileDTO convertToDTO(User user, UserProfile userProfile) {
        UserWithProfileDTO.UserWithProfileDTOBuilder userWithProfileDTOBuilder = UserWithProfileDTO.builder().userId(user.getId()).userName(user.getName());
        return userProfile == null ? userWithProfileDTOBuilder.build() : userWithProfileDTOBuilder
                .avatarUrl(userProfile.getAvatarUrl())
                .contactSubscription(Map.of("EMAIL", userProfile.getEmail()))
                .email(userProfile.getEmail())
                .build();
    }
}