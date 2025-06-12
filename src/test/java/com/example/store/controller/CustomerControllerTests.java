package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@ComponentScan(basePackageClasses = CustomerMapper.class)
class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private CustomerMapper customerMapper;

    
    @Autowired
    private CustomerController customerController;


    @MockitoBean
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("John Doe");
        customer.setId(1L);
    }

    @Test
    void testCreateCustomer() throws Exception {
        when(customerRepository.save(customer)).thenReturn(customer);

        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testGetAllCustomers() throws Exception {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        mockMvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..name").value("John Doe"));
        
    }

    @Test
    void searchCustomersWwithQueryReturnsFiltered() {
        String query = "john";
        Customer customer1 = new Customer(1L, "John Doe");
        Customer customer2 = new Customer(2L, "Johnny Smith");
        CustomerDTO dto1 = customerMapper.customerToCustomerDTO(customer1);
        CustomerDTO dto2 = customerMapper.customerToCustomerDTO(customer2);

        when(customerRepository.findByNameContainingIgnoreCase(query))
            .thenReturn(Arrays.asList(customer1, customer2));
        when(customerMapper.customerToCustomerDTO(customer1)).thenReturn(dto1);
        when(customerMapper.customerToCustomerDTO(customer2)).thenReturn(dto2);

        List<CustomerDTO> result = customerController.searchCustomers(query);

        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Johnny Smith", result.get(1).getName());
        verify(customerRepository).findByNameContainingIgnoreCase(query);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void searchCustomersEmptyQueryReturnsAllCustomers() {
        Customer customer1 = new Customer(1L, "Alice Wonder");
        Customer customer2 = new Customer(2L, "Bob Builder");
        CustomerDTO dto1 = customerMapper.customerToCustomerDTO(customer1);
        CustomerDTO dto2 = customerMapper.customerToCustomerDTO(customer2);


        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));
        when(customerMapper.customersToCustomerDTOs(anyList()))
            .thenReturn(Arrays.asList(dto1, dto2));

        List<CustomerDTO> result = customerController.searchCustomers(null);

        assertEquals(2, result.size());
        verify(customerRepository).findAll();
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void searchCustomers_withBlankQuery_returnsAllCustomers() {
        Customer customer = new Customer(1L, "Test Customer");
        CustomerDTO dto = customerMapper.customerToCustomerDTO(customer);

        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));
        when(customerMapper.customersToCustomerDTOs(anyList()))
            .thenReturn(Collections.singletonList(dto));

        List<CustomerDTO> result = customerController.searchCustomers("   ");

        assertEquals(1, result.size());
        verify(customerRepository).findAll();
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void searchCustomers_noMatchingResults_returnsEmptyList() {
        String query = "nonexistent";
        when(customerRepository.findByNameContainingIgnoreCase(query))
            .thenReturn(Collections.emptyList());

        List<CustomerDTO> result = customerController.searchCustomers(query);

        assertTrue(result.isEmpty());
        verify(customerRepository).findByNameContainingIgnoreCase(query);
        verifyNoMoreInteractions(customerRepository);
    }


}
