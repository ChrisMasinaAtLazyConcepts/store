package com.example.store.service;

import com.example.store.entity.Customer;
import com.example.store.repository.CustomerRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

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
        Optional<Customer> customer =repository.findById(id);
        if(!customer.isPresent()){
            throw new NoSuchElementException();
        }
        return repository.findById(id).get();
    }

    @Cacheable("customers")
    public Page<Customer> getAllCustomers(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Customer> searchCustomer(String query, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(query, pageable);
    }

    public Customer createCustomer(Customer customer) {
        return repository.save(customer);
    }
}
