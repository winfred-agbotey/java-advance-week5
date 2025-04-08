package com.mawulidev.week5labs.repositories;

import com.mawulidev.week5labs.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUsersByEmail(String username);
}
