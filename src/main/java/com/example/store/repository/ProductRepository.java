package com.example.store.repository;

import com.example.store.entity.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // List<Product> findByOrdersId(List<Long> orderId);
    @Query("SELECT p.id FROM Product p JOIN p.orders o WHERE o.id IN :orderIds")
    List<Long> findProductIdsByOrdersIdIn(@Param("orderIds") List<Long> orderIds);
}