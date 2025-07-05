package com.mgaye.bsys.init;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mgaye.bsys.model.Role;
import com.mgaye.bsys.model.User;
import com.mgaye.bsys.model.Role.ERole;
import com.mgaye.bsys.repository.RoleRepository;
import com.mgaye.bsys.repository.UserRepository;
import com.mgaye.bsys.service.CurrencyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrencyService currencyService; // Added injection

    @Override
    @Transactional
    public void run(String... args) {
        createRoles();
        createAdminUser();
        seedCurrencies();
    }

    private void createRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(List.of(
                    new Role(ERole.ROLE_ADMIN, "System Administrator"),
                    new Role(ERole.ROLE_USER, "Regular User"),
                    new Role(ERole.ROLE_AUDITOR, "Financial Auditor")));
        }
    }

    private void createAdminUser() {
        if (!userRepository.existsByEmail("admin@bank.com")) {
            User admin = User.builder()
                    .email("admin@bank.com")
                    .password(passwordEncoder.encode("SecureAdmin123!"))
                    .firstName("System")
                    .lastName("Administrator")
                    .phone("+1234567890")
                    .dob(LocalDate.of(1970, 1, 1)) // Add a default date of birth
                    .active(true) // Ensure active is set
                    .isEnable(true) // Ensure enabled is set
                    .build();

            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not found in database"));

            admin.getRoles().add(adminRole);
            userRepository.save(admin);
        }
    }

    private void seedCurrencies() {
        try {
            currencyService.createCurrency("USD", "US Dollar", 2);
            currencyService.createCurrency("EUR", "Euro", 2);
            currencyService.createCurrency("XOF", "CFA Franc", 0);
            currencyService.createCurrency("JPY", "Japanese Yen", 0);
        } catch (Exception e) {
            log.error("Currency seeding failed", e);
        }
    }
}