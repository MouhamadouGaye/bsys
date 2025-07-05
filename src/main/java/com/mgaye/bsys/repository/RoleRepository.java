package com.mgaye.bsys.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mgaye.bsys.model.Role;
import com.mgaye.bsys.model.Role.ERole;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);

}
