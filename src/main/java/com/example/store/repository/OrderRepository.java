package com.example.store.repository;

import com.example.store.entity.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o.id FROM Order o JOIN o.products p WHERE p.id IN :productIds")
    List<Long> findOrderIdsByProductIds(@Param("productIds") List<Long> productIds);

    List<Order> findByCustomerId(Long customerId);

    List<Order> findAll();
}
