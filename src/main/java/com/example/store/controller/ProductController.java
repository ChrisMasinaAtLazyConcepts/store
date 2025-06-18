package com.example.store.controller;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/store/products")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "Product Controller API")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @Operation(summary = "Create product")
    @ApiResponses(value = {@ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "400")})
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        try {
            if (productDTO == null) {
                return ResponseEntity.badRequest().body("Error creating product cannot be null");
            }
            Product product = productService.createProduct(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating product");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        if (id == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product id cannot be null or zero");
        }
        Product product = productService.getProductById(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping
    @Operation(summary = "Get all products")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    public ResponseEntity<?> getAllProducts() {
        try {
            List<ProductDTO> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting products");
        }
    }
    

    @GetMapping("/by-orders")
    @Operation(summary = "Get product IDs by order IDs")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "400")})
    public ResponseEntity<?> getProductIdsByOrderIds(@RequestParam(required = false) List<Long> orderIds) {
        try {
            if (orderIds == null || orderIds.isEmpty()) {
                return ResponseEntity.badRequest().body("orderIds cannot be null or empty");
            }
            List<Long> productIds = productService.getProductIdsByOrderIds(orderIds);
            return ResponseEntity.ok(productIds);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting product IDs");
        }
    }

}
