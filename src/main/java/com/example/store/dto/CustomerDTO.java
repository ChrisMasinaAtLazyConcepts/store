package com.example.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerDTO {
   
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;

    @JsonIgnore
    private List<OrderDTO> orders;

     public CustomerDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
