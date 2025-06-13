package com.example.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerDTO {

    public CustomerDTO(Long id, String name) 
    {
      this.id=id;
      this.name=name;
    }
    private Long id;
    private String name;
    private List<CustomerOrderDTO> orders;

}
