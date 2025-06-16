package com.example.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import jakarta.validation.constraints.NotBlank;


@NoArgsConstructor
@Data
public class OrderDTO {
    private Long id;

    @NotBlank(message = "description is required")
    private String description;

    private CustomerDTO customer;

    private List<ProductDTO> products;
}
