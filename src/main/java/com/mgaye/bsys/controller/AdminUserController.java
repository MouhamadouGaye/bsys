package com.mgaye.bsys.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgaye.bsys.dto.UserRegistrationDto;
import com.mgaye.bsys.dto.request.RegisterRequest;
import com.mgaye.bsys.dto.request.UserCreateDto;
import com.mgaye.bsys.dto.response.UserResponse;
import com.mgaye.bsys.dto.response.UserResponseDto;
import com.mgaye.bsys.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestBody UserRegistrationDto userCreateDto) {
        UserResponseDto createdUser = userService.createUser(userCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminTest() {
        return ResponseEntity.ok("Admin access successful!");
    }

    // @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<UserResponseDto> createUser(
    // @Valid @RequestBody UserRegistrationDto userCreateDto) {
    // UserResponseDto createdUser = userService.createUser(userCreateDto);
    // return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    // }

}
