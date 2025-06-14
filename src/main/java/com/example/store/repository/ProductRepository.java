package com.example.store.repository;

import com.example.store.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p.id FROM Product p JOIN p.orders o WHERE o.id IN :orderIds")
    List<Long> findProductIdsByOrdersIdIn(@Param("orderIds") List<Long> orderIds);
}
