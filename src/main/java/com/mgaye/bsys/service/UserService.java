package com.mgaye.bsys.service;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mgaye.bsys.dto.UserRegistrationDto;
import com.mgaye.bsys.dto.request.UserCreateDto;
import com.mgaye.bsys.dto.response.UserResponseDto;
import com.mgaye.bsys.exception.ConflictException;
import com.mgaye.bsys.exception.EmailAlreadyExistsException;
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

    // public UserResponseDto createUser(UserRegistrationDto dto) {
    // if (userRepository.existsByEmail(dto.getEmail())) {
    // throw new IllegalStateException("Email already in use");
    // }

    // User user = new User();
    // user.setFirstName(dto.getFirstName());
    // user.setLastName(dto.getLastName());
    // user.setEmail(dto.getEmail());
    // user.setPassword(passwordEncoder.encode(dto.getPassword()));
    // user.setPhone(dto.getPhone());
    // user.setDob(dto.getDob());
    // user.setActive(true);
    // user.setEnable(true);

    // // Assign default role
    // Role userRole = roleRepository.findByName(ERole.ROLE_USER)
    // .orElseThrow(() -> new RoleNotFoundException("ROLE_USER not found"));
    // user.getRoles().add(userRole);

    // User savedUser = userRepository.save(user);
    // return UserResponseDto.fromEntity(savedUser);
    // }

    public UserResponseDto createUser(UserRegistrationDto dto) {
        // Check if email already exists
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        // Create new user
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setDob(dto.getDob());
        user.setActive(dto.isActive());
        user.setEnable(dto.isEnable());

        // Assign default USER role
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException("ROLE_USER not found"));
        user.getRoles().add(userRole);

        // Save the user
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    // private UserResponseDto convertToDto(User user) {
    // UserResponseDto dto = modelMapper.map(user, UserResponseDto.class);
    // dto.setRoles(user.getRoles().stream()
    // .map(role -> role.getName().name())
    // .collect(Collectors.toSet()));
    // return dto;
    // }
    private UserResponseDto convertToDto(User user) {
        UserResponseDto dto = modelMapper.map(user, UserResponseDto.class);
        dto.setRoles(user.getRoles().stream()
                .map(Role::getName) // <-- fix: return ERole instead of String
                .collect(Collectors.toSet()));
        return dto;
    }

}