package com.example.weblogin.domain.user;

import com.example.weblogin.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

// <Entity, Entity-id>
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
