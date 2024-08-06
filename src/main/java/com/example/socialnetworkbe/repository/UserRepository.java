package com.example.socialnetworkbe.repository;

import com.example.socialnetworkbe.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByVerificationToken(String token);
    Page<User> findAll(Pageable pageable);
}