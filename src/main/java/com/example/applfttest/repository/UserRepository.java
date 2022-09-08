package com.example.applfttest.repository;

import com.example.applfttest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByToken(UUID token);
}
