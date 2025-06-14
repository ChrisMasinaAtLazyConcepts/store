package com.example.store.service;

import com.example.store.entity.Customer;
import com.example.store.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository repository;

    public Customer getCustomer(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Cacheable("customers")
    public Page<Customer> getAllCustomers(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Customer> searchCustomer(String query, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(query, pageable);
    }

    public Object createCustomer(Customer customer) {
        return repository.save(customer);
    }
}
