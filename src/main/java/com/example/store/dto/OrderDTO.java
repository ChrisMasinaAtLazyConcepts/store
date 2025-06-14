package com.example.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@NoArgsConstructor
@Data
public class OrderDTO {
    private Long id;

    private String description;

    private CustomerDTO customer;

    private List<ProductDTO> products;
}
