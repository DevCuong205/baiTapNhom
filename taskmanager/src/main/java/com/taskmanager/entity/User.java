package com.taskmanager.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String fullname;

    private String role;

    private String avatar;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks;
}