package com.example.store.service;
import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void testCreateProduct() {
        Product product = new Product();
        ProductDTO productDTO = new ProductDTO();
        when(productMapper.productDTOToProduct(any(ProductDTO.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.productToProductDTO(any(Product.class))).thenReturn(productDTO);

        ProductDTO result = productService.createProduct(productDTO);

        assertEquals(productDTO, result);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = List.of(new Product(), new Product());
        List<ProductDTO> productDTOs = List.of(new ProductDTO(), new ProductDTO());
        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.productToProductDTO(any(Product.class))).thenReturn(productDTOs.get(0), productDTOs.get(1));

        List<ProductDTO> result = productService.getAllProducts();

        assertEquals(productDTOs, result);
    }

    @Test
    void testGetProductById() {
        Product product = new Product();
        ProductDTO productDTO = new ProductDTO();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.productToProductDTO(any(Product.class))).thenReturn(productDTO);

        ProductDTO result = productService.getProductById(1L);

        assertEquals(productDTO, result);
    }
}