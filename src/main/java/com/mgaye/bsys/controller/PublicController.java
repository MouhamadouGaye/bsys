package com.mgaye.bsys.controller;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mgaye.bsys.dto.UserRegistrationDto;
import com.mgaye.bsys.dto.request.LoginRequestDto;
import com.mgaye.bsys.dto.response.UserResponseDto;
import com.mgaye.bsys.model.User;
import com.mgaye.bsys.security.JwtTokenProvider;
import com.mgaye.bsys.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @Valid @RequestBody LoginRequestDto loginRequest) {

        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String jwt = tokenProvider.generateToken(authentication);

            // Get user details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("email", userDetails.getUsername());
            response.put("roles", userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserRegistrationDto dto,
            HttpServletRequest request) {
        try {
            // Register the user
            UserResponseDto createdUser = userService.registerUser(dto);

            // Auto-login after registration
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getEmail(),
                            dto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String jwt = tokenProvider.generateToken(authentication);

            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("user", createdUser);
            response.put("token", jwt);

            return ResponseEntity
                    .created(getLocationUri(request, UUID.fromString(createdUser.getId())))
                    .body(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Auto-login failed after registration");
        }
    }

    private URI getLocationUri(HttpServletRequest request, UUID userId) {
        return ServletUriComponentsBuilder
                .fromContextPath(request)
                .path("/api/users/{id}")
                .buildAndExpand(userId)
                .toUri();
    }

    // @PostMapping("/refresh-token")
    // public ResponseEntity<?> refreshToken(HttpServletRequest request) {
    // String refreshToken = tokenProvider.parseJwt(request);
    // // Validate and generate new token
    // return ResponseEntity.ok().body("Refresh token endpoint is under
    // construction");
    // }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String token = tokenProvider.parseJwt(request);
        if (token != null && tokenProvider.validateJwtToken(token)) {
            String username = tokenProvider.getUserNameFromJwtToken(token);
            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // Create authentication
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            // Generate new token
            String newToken = tokenProvider.generateToken(authentication);
            return ResponseEntity.ok(Collections.singletonMap("token", newToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

}
