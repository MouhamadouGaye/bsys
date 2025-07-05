package com.mgaye.bsys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mgaye.bsys.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

}
