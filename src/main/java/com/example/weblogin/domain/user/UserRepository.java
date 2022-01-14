package com.example.weblogin.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

// <Entity, Entity-id>
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
