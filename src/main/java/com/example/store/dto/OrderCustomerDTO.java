package com.example.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCustomerDTO {
   
    private Long id;
    private String name;
}
