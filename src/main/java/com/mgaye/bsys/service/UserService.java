package com.mgaye.bsys.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mgaye.bsys.dto.UserRegistrationDto;
import com.mgaye.bsys.dto.response.UserResponseDto;
import com.mgaye.bsys.exception.ConflictException;
import com.mgaye.bsys.exception.RoleNotFoundException;
import com.mgaye.bsys.model.Role;
import com.mgaye.bsys.model.User;
import com.mgaye.bsys.model.Role.ERole;
import com.mgaye.bsys.repository.RoleRepository;
import com.mgaye.bsys.repository.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    // @Transactional
    // public UserResponseDto registerUser(UserRegistrationDto dto) {
    // // Validate password match
    // if (!dto.getPassword().equals(dto.getConfirmPassword())) {
    // throw new ValidationException("Passwords do not match");
    // }

    // // Check for existing email
    // if (userRepository.existsByEmail(dto.getEmail())) {
    // throw new ConflictException("Email already registered");
    // }

    // // Map DTO to User entity
    // User user = modelMapper.map(dto, User.class);
    // user.setPassword(passwordEncoder.encode(dto.getPassword()));
    // user.setActive(true);

    // // Add default USER role
    // Role userRole = roleRepository.findByName("ROLE_USER");
    // if (userRole != null){
    // throw new RoleNotFoundException("ROLE_USER not found"));
    // }
    // user.addRole(userRole);

    // // Save user
    // User savedUser = userRepository.save(user);

    // // Return response DTO
    // return modelMapper.map(savedUser, UserResponseDto.class);
    // }

    @Transactional
    public UserResponseDto registerUser(UserRegistrationDto dto) {
        // Validate password match
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new ValidationException("Passwords do not match");
        }

        // Check for existing email
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("Email already registered");
        }

        // Map DTO to User entity
        User user = modelMapper.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setActive(true);

        // Add default USER role
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException("ROLE_USER not found"));
        user.addRole(userRole);

        // Save user
        User savedUser = userRepository.save(user);

        // Return response DTO
        return modelMapper.map(savedUser, UserResponseDto.class);
    }
}