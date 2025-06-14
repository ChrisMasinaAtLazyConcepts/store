package com.example.store.entity;

import jakarta.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "\"store_user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String username;
    private String password;
    private String email;
    private String role;

    public User() {}
}
