package com.example.store.dto;

import lombok.Data;
import java.util.Set;

@Data
public class ProductDTO {
    private Long id;
    private String description;
    private Set<Long> orderIds;
}