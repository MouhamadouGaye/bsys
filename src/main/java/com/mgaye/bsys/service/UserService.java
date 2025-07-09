package com.mgaye.bsys.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mgaye.bsys.dto.UserRegistrationDto;
import com.mgaye.bsys.dto.request.RegisterRequest;
import com.mgaye.bsys.dto.response.UserResponse;
import com.mgaye.bsys.dto.response.UserResponseDto;
import com.mgaye.bsys.model.User;

@Service
public interface UserService {
    // UserResponseDto createUser(UserRegistrationDto request);

    UserResponseDto createUser(UserRegistrationDto dto);

    UserResponseDto getUserDtoById(String userId);

    boolean existsByEmail(String email);

    UserResponseDto getCurrentUser(String userId);

    Optional<User> findById(String userId);
}
