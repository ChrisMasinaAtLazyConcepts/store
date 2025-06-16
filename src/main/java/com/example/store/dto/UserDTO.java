package com.example.store.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    private Long id;
    
    @NotNull
    private String username;
    
     @NotNull
    private String email;
    
     @NotNull
    private String password;
}
