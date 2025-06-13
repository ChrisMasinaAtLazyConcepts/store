package com.example.store.entity;

import jakarta.persistence.*;

import lombok.Data;



@Entity
@Data
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    public User() {
    }

}
