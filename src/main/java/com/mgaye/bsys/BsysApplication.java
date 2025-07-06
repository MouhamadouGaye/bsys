package com.mgaye.bsys;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Component;

import com.mgaye.bsys.model.Role;
import com.mgaye.bsys.model.Role.ERole;
import com.mgaye.bsys.repository.RoleRepository;

@SpringBootApplication
@EntityScan(basePackages = "com.mgaye.bsys.model")
public class BsysApplication {

	public static void main(String[] args) {
		SpringApplication.run(BsysApplication.class, args);
	}

	// @Component
	// public class DataInitializer implements CommandLineRunner {
	// private final RoleRepository roleRepository;

	// public DataInitializer(RoleRepository roleRepository) {
	// this.roleRepository = roleRepository;
	// }

	// @Override
	// public void run(String... args) throws Exception {
	// // Check if roles exist, if not create them
	// if (roleRepository.findByName(ERole.ROLE_ADMIN) == null) {
	// Role adminRole = new Role("ROLE_ADMIN");
	// roleRepository.save(adminRole);
	// }
	// // ... similarly for other roles
	// }
	// }
}
