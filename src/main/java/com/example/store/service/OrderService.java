package com.example.store.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.store.entity.Order;
import com.example.store.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    OrderRepository repository;

    
    public Order createOrder(Order order) {
        return repository.save(order);
    }
    
    
    public List<Order> getOrderByCustomerId(String customerId) {
        return repository.findByCustomerId(Long.parseLong(customerId));
    }
    
    public List<Order> getAllOrders() {
        return repository.findAll();
    }

     public Order findById(Long id) {
        return repository.findById(id).orElse(null);
    }

}