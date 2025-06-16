package com.example.store.integration;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CustomerIntegrationTests {

   @Autowired
    private MockMvc mockMvc;


    private Customer testCustomer;

    @Autowired
    private ObjectMapper objectMapper;


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
    public void testCreateCustomer() throws Exception {
        CustomerDTO customer = new CustomerDTO();
        customer.setName("John Doe");

        mockMvc.perform(post("/store/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"));
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


    
}