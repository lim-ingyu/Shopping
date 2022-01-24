package com.example.weblogin.service;

import com.example.weblogin.domain.user.User;
import com.example.weblogin.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CartService cartService;

    @Transactional // Write(Insert, Update, Delete)
    public User signup(User user) {
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        user.setRole("ROLE_USER");

        User userEntity = userRepository.save(user);
        cartService.createCart(user);
        return userEntity;
    }

    @Transactional // Write(Insert, Update, Delete)
    public User userUpdate(User user) {
        User userEntity = userRepository.save(user);
        return userEntity;
    }
}
