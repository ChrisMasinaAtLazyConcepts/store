package com.example.store.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private String description;
    private OrderCustomerDTO customer;
    private List<ProductDTO> products;

}
