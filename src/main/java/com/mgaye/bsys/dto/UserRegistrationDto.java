package com.mgaye.bsys.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegistrationDto {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100, message = "First name must be 2-100 characters")
    private String firstName;

    @NotBlank(message = "last name is required")
    @Size(min = 2, max = 100, message = "last name must be 2-100 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9\\s]{8,15}$", message = "Invalid phone number format")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must contain uppercase, lowercase, number and special character")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    @NotNull(message = "Date of birth is required") // Changed to @NotNull
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob; // Removed @NotBlank

    private boolean active = true;
    private boolean enable = true;

    @AssertTrue(message = "Passwords must match")
    private boolean isPasswordsMatch() {
        return password != null && password.equals(confirmPassword);
    }
}