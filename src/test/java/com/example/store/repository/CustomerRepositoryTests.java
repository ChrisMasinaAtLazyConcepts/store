package com.example.store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.example.store.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTests {

    @Autowired
    CustomerRepository customerRepository;


    @Test
    public void testFindByNameContainingIgnoreCase() {

        Page<Customer> result = customerRepository.findByNameContainingIgnoreCase("Vicki", PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
    }

}