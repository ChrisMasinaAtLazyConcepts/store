package com.example.store.service;

import com.example.store.entity.Order;
import com.example.store.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

    public List<Order> getOrdersByIds(Set<Long> orderIds) {
        return repository.findByIdIn(orderIds);
    }
}
