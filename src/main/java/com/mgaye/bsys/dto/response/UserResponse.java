package com.mgaye.bsys.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.mgaye.bsys.model.Role;

import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String fullName;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate dob;
    private LocalDateTime createdAt;
    private String deviceToken;
    private Set<Role> roles;
    private boolean active;
    private String kycStatus; // Added to match your mapper

}
