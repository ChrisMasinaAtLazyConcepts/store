package com.example.store.entity;

import jakarta.persistence.*;

import lombok.Data;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_id_seq", allocationSize = 1)
    private Long id;

    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "products")
    private List<Order> orders;
}
