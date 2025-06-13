package com.example.store.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    
    @Autowired
    ProductMapper productMapper;
    
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        Product product = productMapper.productDTOToProduct(productDTO);
        return productMapper.productToProductDTO(productRepository.save(product));
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
            .stream()
            .map(productMapper::productToProductDTO)
            .toList();
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
            .map(productMapper::productToProductDTO)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @GetMapping("/by-orders")
    public List<Long> getProductIdsByOrderIds(@RequestParam List<Long> orderIds) {
        return productRepository.findProductIdsByOrdersIdIn(orderIds);
    }
    
}