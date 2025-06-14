package com.example.store.entity;

import jakarta.persistence.*;

import lombok.Data;

import java.util.List;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String description;

    @ManyToMany(mappedBy = "products")
    private List<Order> orders;
}
