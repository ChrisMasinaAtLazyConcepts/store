package com.example.store.service;

import com.example.store.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {

    @Mock
    private CustomerService customerService;

    @Test
    void testGetCustomer() {
        Customer customer = new Customer();
        when(customerService.getCustomer(1L)).thenReturn(customer);

        Customer result = customerService.getCustomer(1L);

        assertEquals(customer, result);
    }

    @Test
    void testGetAllCustomers() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Customer> customers = List.of(new Customer(), new Customer());
        Page<Customer> page = new PageImpl<>(customers);
        when(customerService.getAllCustomers(pageable)).thenReturn(page);

        Page<Customer> result = customerService.getAllCustomers(pageable);

        assertEquals(customers, result.getContent());
    }

    @Test
    void testSearchCustomer() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Customer> customers = List.of(new Customer(), new Customer());
        Page<Customer> page = new PageImpl<>(customers);
        when(customerService.searchCustomer("query", pageable)).thenReturn(page);

        Page<Customer> result = customerService.searchCustomer("query", pageable);

        assertEquals(customers, result.getContent());
    }

    @Test
    void testCreateCustomer() {
        Customer customer = new Customer();
        when(customerService.createCustomer(any(Customer.class))).thenReturn(customer);

        Object result = customerService.createCustomer(customer);

        assertEquals(customer, result);
    }
} 