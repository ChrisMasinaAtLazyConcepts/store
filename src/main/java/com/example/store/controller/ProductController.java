package com.example.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.service.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        return productService.createProduct(productDTO);
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
      return productService.getProductById(id);
    }

    @GetMapping("/by-orders")
    public List<Long> getProductIdsByOrderIds(@RequestParam List<Long> orderIds) {
        return productService.getProductIdsByOrderIds(orderIds);
    }
}