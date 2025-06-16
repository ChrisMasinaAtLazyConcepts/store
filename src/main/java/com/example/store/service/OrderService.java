package com.example.store.service;

import com.example.store.entity.Order;
import com.example.store.repository.OrderRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

   @Transactional
    public Order createOrder(@Valid Order order) {
        log.info("Creating order: {}", order);
        return repository.save(order);
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Transactional
    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public List<Order> getOrdersByIds(Set<Long> orderIds) {
        return repository.findByIdIn(orderIds);
    }

   public List<Long> getOrderIdsByProductIds(List<Long> productIds) {
    return repository.findOrderIdsByProductIds(productIds);
}

}
