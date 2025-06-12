package com.example.store.dto;

import java.util.Set;

import lombok.Data;

@Data
public class OrderDTO {
    private Long id;
    private String description;
    private OrderCustomerDTO customer;
    private Set<ProductDTO> products;

     public OrderDTO(String description, OrderCustomerDTO customer) {
        this.description=description;
        this.customer=customer;
    }
}
