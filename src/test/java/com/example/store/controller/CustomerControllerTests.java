package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.OrderRepository;
import com.example.store.security.JwtUserDetailsService;
import com.example.store.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean 
    private CustomerService customerService;

    @MockitoBean
    private CustomerMapper customerMapper;

    @MockitoBean
    private JwtUserDetailsService jwtUserDetailsService;

    @MockitoBean
    private OrderRepository orderRepository;


    @Test
    public void testGetAllCustomers() throws Exception {
        Customer customer = new Customer();
        customer.setName("Test Customer");

        Page<Customer> page = new PageImpl<>(List.of(customer));
        when(customerService.getAllCustomers(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/store/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testSearchCustomers() throws Exception {
        Customer customer = new Customer();
        customer.setName("Test Customer");

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("Test Customer");

        Page<Customer> page = new PageImpl<>(List.of(customer));
        when(customerService.searchCustomer(any(String.class), any(Pageable.class)))
                .thenReturn(page);

        when(customerMapper.customerToCustomerDTO(any(Customer.class)))
                .thenReturn(customerDTO);

        mockMvc.perform(get("/store/customers/search").param("query", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Customer"));
    }

    @Test
    public void testSearchCustomersNoQuery() throws Exception {
        Customer customer = new Customer();
        customer.setName("Test Customer");

        Page<Customer> page = new PageImpl<>(List.of(customer));
        when(customerService.searchCustomer(any(String.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/store/customers/search"))
                .andExpect(status().isInternalServerError());
    }


    @Test
    public void testSearchCustomersPagination() throws Exception {
        Customer customer = new Customer();
        customer.setName("Test Customer");

        Page<Customer> page = new PageImpl<>(List.of(customer));
        when(customerService.searchCustomer(any(String.class), any(Pageable.class)))
                .thenReturn(page);
        mockMvc.perform(get("/store/customers/search")
                .param("query", "test")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testSearchCustomersPaginationPageSize() throws Exception {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Customer customer = new Customer();
            customer.setName("Test Customer " + i);
            customers.add(customer);
        }

        Page<Customer> page = new PageImpl<>(customers.subList(0, 10), PageRequest.of(0, 10), customers.size());
        when(customerService.searchCustomer(any(String.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/store/customers/search")
                .param("query", "test")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10));
    }

    @Test
    public void testSearchCustomersErrorHandling() throws Exception {
        when(customerService.searchCustomer(any(String.class), any(Pageable.class)))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/store/customers/search").param("query", "test"))
                .andExpect(status().isInternalServerError());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
