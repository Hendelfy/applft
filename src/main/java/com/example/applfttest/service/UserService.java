package com.example.applfttest.service;

import com.example.applfttest.domain.User;
import com.example.applfttest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User getByToken(String token) {
        try {
            UUID uuid = UUID.fromString(token);
            return repository.findByToken(uuid);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    public User getReferenceById(long userId) {
        return repository.getReferenceById(userId);
    }
}
