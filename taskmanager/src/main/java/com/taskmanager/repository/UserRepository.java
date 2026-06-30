package com.taskmanager.repository;

import com.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    List<User> findByFullnameContainingIgnoreCaseOrUsernameContainingIgnoreCase(
            String fullname,
            String username
    );
}