package com.example.store.entity;

import jakarta.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "\"store_user\"")
public class User {

 
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_id_seq", allocationSize = 1)

    private String username;
    private String password;
    private String email;
    private String role;

    public User() {}
}
