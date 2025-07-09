package com.mgaye.bsys.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mgaye.bsys.dto.UserRegistrationDto;
import com.mgaye.bsys.dto.request.RegisterRequest;
import com.mgaye.bsys.dto.response.UserResponse;
import com.mgaye.bsys.dto.response.UserResponseDto;
import com.mgaye.bsys.exception.EmailAlreadyExistsException;
import com.mgaye.bsys.exception.ResourceNotFoundException;
import com.mgaye.bsys.exception.RoleNotFoundException;
import com.mgaye.bsys.model.Role;
import com.mgaye.bsys.model.Role.ERole;
import com.mgaye.bsys.model.User;
import com.mgaye.bsys.repository.RoleRepository;
import com.mgaye.bsys.repository.UserRepository;
import com.mgaye.bsys.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    // private final UserRepository userRepository;
    // private final RoleRepository roleRepository;
    // private final PasswordEncoder passwordEncoder;
    // private final ModelMapper modelMapper;

    // // @Transactional
    // // public UserResponseDto registerUser(UserRegistrationDto dto) {
    // // // Validate password match
    // // if (!dto.getPassword().equals(dto.getConfirmPassword())) {
    // // throw new ValidationException("Passwords do not match");
    // // }

    // // // Check for existing email
    // // if (userRepository.existsByEmail(dto.getEmail())) {
    // // throw new ConflictException("Email already registered");
    // // }

    // // // Map DTO to User entity
    // // User user = modelMapper.map(dto, User.class);
    // // user.setPassword(passwordEncoder.encode(dto.getPassword()));
    // // user.setActive(true);

    // // // Add default USER role
    // // Role userRole = roleRepository.findByName("ROLE_USER");
    // // if (userRole != null){
    // // throw new RoleNotFoundException("ROLE_USER not found"));
    // // }
    // // user.addRole(userRole);

    // // // Save user
    // // User savedUser = userRepository.save(user);

    // // // Return response DTO
    // // return modelMapper.map(savedUser, UserResponseDto.class);
    // // }

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
    // Role userRole = roleRepository.findByName(ERole.ROLE_USER)
    // .orElseThrow(() -> new RoleNotFoundException("ROLE_USER not found"));
    // user.addRole(userRole);

    // // Save user
    // User savedUser = userRepository.save(user);

    // // Return response DTO
    // return modelMapper.map(savedUser, UserResponseDto.class);
    // }

    // // public UserResponseDto createUser(UserRegistrationDto dto) {
    // // if (userRepository.existsByEmail(dto.getEmail())) {
    // // throw new IllegalStateException("Email already in use");
    // // }

    // // User user = new User();
    // // user.setFirstName(dto.getFirstName());
    // // user.setLastName(dto.getLastName());
    // // user.setEmail(dto.getEmail());
    // // user.setPassword(passwordEncoder.encode(dto.getPassword()));
    // // user.setPhone(dto.getPhone());
    // // user.setDob(dto.getDob());
    // // user.setActive(true);
    // // user.setEnable(true);

    // // // Assign default role
    // // Role userRole = roleRepository.findByName(ERole.ROLE_USER)
    // // .orElseThrow(() -> new RoleNotFoundException("ROLE_USER not found"));
    // // user.getRoles().add(userRole);

    // // User savedUser = userRepository.save(user);
    // // return UserResponseDto.fromEntity(savedUser);
    // // }

    // public UserResponseDto createUser(UserRegistrationDto dto) {
    // // Check if email already exists
    // if (userRepository.existsByEmail(dto.getEmail())) {
    // throw new EmailAlreadyExistsException("Email already in use");
    // }

    // // Create new user
    // User user = new User();
    // user.setFirstName(dto.getFirstName());
    // user.setLastName(dto.getLastName());
    // user.setEmail(dto.getEmail());
    // user.setPassword(passwordEncoder.encode(dto.getPassword()));
    // user.setPhone(dto.getPhone());
    // user.setDob(dto.getDob());
    // user.setActive(dto.isActive());
    // user.setEnable(dto.isEnable());

    // // Assign default USER role
    // Role userRole = roleRepository.findByName(ERole.ROLE_USER)
    // .orElseThrow(() -> new RoleNotFoundException("ROLE_USER not found"));
    // user.addRole(userRole);

    // // Save the user
    // User savedUser = userRepository.save(user);
    // return convertToDto(savedUser);
    // }

    // // private UserResponseDto convertToDto(User user) {
    // // UserResponseDto dto = modelMapper.map(user, UserResponseDto.class);
    // // dto.setRoles(user.getRoles().stream()
    // // .map(role -> role.getName().name())
    // // .collect(Collectors.toSet()));
    // // return dto;
    // // }
    private UserResponseDto convertToDto(User user) {
        UserResponseDto dto = modelMapper.map(user, UserResponseDto.class);
        dto.setRoles(user.getRoles().stream()
                .map(Role::getName) // <-- fix: return ERole instead of String
                .collect(Collectors.toSet()));
        return dto;
    }

    // --------------------------------------------------

    @Override
    @Transactional
    public UserResponseDto createUser(UserRegistrationDto request) {
        if (existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        // User user = userMapper.fromRegisterRequest(request);
        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Assign default USER role
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException("ROLE_USER not found"));
        user.addRole(userRole);

        // Save the user
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);

    }

    // @Override
    // @Transactional
    // public UserResponseDto createUser(UserRegistrationDto request) {
    // if (existsByEmail(request.getEmail())) {
    // throw new EmailAlreadyExistsException(request.getEmail());
    // }

    // // User user = userMapper.fromRegisterRequest(request);
    // User user = modelMapper.map(request, User.class);
    // user.setPassword(passwordEncoder.encode(request.getPassword()));

    // Role userRole = roleRepository.findByName(ERole.ROLE_USER)
    // .orElseThrow(() -> new RuntimeException("Error: Role not found."));

    // Set<Role> roles = new HashSet<>();
    // roles.add(userRole);
    // user.setRoles(roles);

    // User savedUser = userRepository.save(user);
    // return convertToDto(savedUser);

    // }

    @Override
    public UserResponseDto getUserDtoById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override

    public UserResponseDto getCurrentUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return modelMapper.map(user, UserResponseDto.class);
    }

    public Optional<User> findById(String userId) {
        return userRepository.findById(userId);
    }

}
