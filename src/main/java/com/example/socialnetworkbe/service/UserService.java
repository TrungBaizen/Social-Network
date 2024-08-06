package com.example.socialnetworkbe.service;

import com.example.socialnetworkbe.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    void update(User user);
    User save(User user);
    User findByVerificationToken(String token);
    void verifyUser(User user);
    String resetPassword(String to);
    Iterable<User> findAll();

    User findByEmail(String email);

    User getCurrentUser();

    Optional<User> findById(Long id);

    UserDetails loadUserById(Long id);
    public Page<User> getAllUsers(Pageable pageable);
    boolean checkLogin(User user);

    boolean isRegister(User user);

    boolean isCorrectConfirmPassword(User user);

    void setActive(User user , boolean isActive);
}