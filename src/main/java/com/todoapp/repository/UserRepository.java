package com.todoapp.repository;

import com.todoapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom method to find a user by their username (required by Spring Security)
    User findByUsername(String username);
}