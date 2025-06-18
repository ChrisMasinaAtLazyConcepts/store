package com.example.store.service;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    public Product createProduct(ProductDTO productDTO) {
        Product product = productMapper.productDTOToProduct(productDTO);
        return productRepository.save(product);
    }

 
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::productToProductDTO)
                .toList();
    }


    public Product getProductById(@PathVariable Long id) {
        if(id == 0){
            new BadRequestException("Product id cannot be null ");
        }
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    public List<Long> getProductIdsByOrderIds(@RequestParam List<Long> orderIds) {
        
        return productRepository.findProductIdsByOrdersIdIn(orderIds);
    }
}
