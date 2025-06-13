package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;


@WebMvcTest(CustomerController.class)
public class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockitoBean
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("John Doe");
        customer.setId(1L);

        CustomerDTO customerDTO = new CustomerDTO();
        when(customerMapper.customerToCustomerDTO(any(Customer.class))).thenReturn(customerDTO);
    }

    @Test
    void testCreateCustomer() throws Exception {
        when(customerService.createCustomer(customer)).thenReturn(customer);

        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }
    
   @Test
    void testGetAllCustomers() throws Exception {
        // Create test data with pagination
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> customerPage = new PageImpl<>(
            Collections.emptyList(), 
            pageable, 
            0
        );

        when(customerService.getAllCustomers(pageable)).thenReturn(customerPage);

        mockMvc.perform(get("/api/customers")
               .param("page", "0")
               .param("size", "10"))
               .andExpect(status().isOk());
    }

   @Test
    void searchCustomersWithQueryReturnsFiltered() throws Exception {
        Customer customer1 = new Customer(1L, "John Doe");
        Customer customer2 = new Customer(2L, "Johnny Smith");
        
        Page<Customer> customerPage = new PageImpl<>(
            List.of(customer1, customer2),
            PageRequest.of(0, 10),
            2
        );

        when(customerService.searchCustomer(customer1.getName(),any(Pageable.class))).thenReturn(customerPage);
        when(customerMapper.customerToCustomerDTO(customer1)).thenReturn(new CustomerDTO(1L, "John Doe", List.of()));
        when(customerMapper.customerToCustomerDTO(customer2)).thenReturn(new CustomerDTO(2L, "Johnny Smith", List.of()));

        mockMvc.perform(get("/api/customers/search")
            .param("query", "john")
            .param("page", "0")
            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[*].name", 
                    containsInAnyOrder("John Doe", "Johnny Smith")))
            .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void searchCustomersEmptyQueryReturnsAllCustomers() throws Exception {
        Customer customer1 = new Customer(1L, "Alice Wonder");
        Customer customer2 = new Customer(2L, "Bob Builder");
        Page<Customer> customerPage = new PageImpl<>(
                    List.of(customer1, customer2),
                    PageRequest.of(0, 10),
                    2
        );

      

        when(customerService.getAllCustomers(any(Pageable.class)))
            .thenReturn(customerPage);

        mockMvc.perform(get("/customers")
                .param("query", "")
                .param("page", "1")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..name").value(containsInAnyOrder("Alice Wonder", "Bob Builder")));
    }

    @Test
    void searchCustomersNoMatchingResults() throws Exception {
        Page<Customer> customerBlank = new PageImpl<>(
                    List.of(),
                    PageRequest.of(0, 10),
                    0
        );
        when(customerService.searchCustomer(anyString(), any(Pageable.class)))
            .thenReturn(customerBlank);

        mockMvc.perform(get("/customers")
                .param("query", "nonexistent")
                .param("page", "1")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }
}
