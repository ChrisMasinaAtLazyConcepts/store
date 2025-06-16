package com.example.store.integration;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CustomerIntegrationTests {

   @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer testCustomer;


    @AfterEach
    public void cleanup() {
        Mockito.reset();
    }

    @BeforeEach
    public void setup() {
        testCustomer = new Customer();
        testCustomer.setName("Test Customer");
    }

    
  @Test
    public void getAllCustomersReturnPaginatedResults() throws Exception {
            mockMvc.perform(get("/store/customers")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

   @Test
    public void searchCustomersReturnMatchingResults() throws Exception {

        MvcResult result = mockMvc.perform(get("/store/customers/search")
                .param("query", "Vicki")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println(responseBody);
    }

    @Test
    public void getCustomerByIdReturnCustomer() throws Exception {
        mockMvc.perform(get("/store/customers/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Muriel Donnelly"));
    }

    @Test
    public void getCustomerByIdReturnNotFound() throws Exception {
        long nonExistentId = 999L;
        mockMvc.perform(get("/store/customers/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }


    @Test
    public void createCustomerInvalidDataShouldReturnBadRequest() throws Exception {
        CustomerDTO invalidCustomer = new CustomerDTO();
        invalidCustomer.setName(""); // Empty name

        mockMvc.perform(post("/store/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCustomer)))
                .andExpect(status().isBadRequest());
    }
}