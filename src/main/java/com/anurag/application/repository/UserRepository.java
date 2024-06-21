package com.anurag.application.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anurag.application.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByMobileNo(String mobileNo);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByMobileNo(String mobileNo);
}
