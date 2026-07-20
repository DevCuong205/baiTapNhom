package com.taskmanager.repository;

import com.taskmanager.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    Page<User> findByFullnameContainingIgnoreCaseOrUsernameContainingIgnoreCase(
            String fullname,
            String username,
            Pageable pageable
    );
}