package com.mgaye.bsys.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.mgaye.bsys.model.Role;
import com.mgaye.bsys.model.User;
import com.mgaye.bsys.model.Role.ERole;

@Data
public class UserResponseDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDate dob;
    private Set<Role.ERole> roles;

    // Add this custom mapping method
    public static UserResponseDto fromEntity(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setActive(user.isActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setDob(user.getDob());

        // Map roles
        Set<Role.ERole> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        dto.setRoles(roleNames);

        return dto;
    }
}

// {
// "id":"6383ce86-0106-4b2b-9013-4dec20f4e17a",
// "firstName":"hawa",
// "lastName":"gaye",
// "email":"hawa.gaye@example.com",
// "phone":"+1664567999",
// "active":true,
// "createdAt":"2025-07-06T20:31:35.238723",
// "dob":"2000-08-09",
// "roles":["ROLE_USER"]
// }
/// --------------------------------------------------------------------------------|
// THE ONE BELOW String were apply to
// roles-----------------------------------------|
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// public class UserResponseDto {
// private String id;
// private String firstName;
// private String lastName;
// private String email;
// private String phone;
// private LocalDate dob;
// private boolean active;
// private boolean enable;
// private Set<String> roles;
// }

// {"id":"159a2b15-49cf-40ab-8615-16ff7234eefa","firstName":"ameth","lastName":"gaye","email":"ameth.gaye@example.com","phone":"+1434567999","dob":"1992-08-09","active":true,"enable":true,"roles":["ROLE_USER"]}%