package com.example.store.controller;

import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import com.example.store.mapper.CustomerMapper;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;
import com.example.store.mapper.ProductMapper;
import com.example.store.dto.ProductDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductRepository productRepository;

    @MockitoBean
    private ProductMapper productMapper;

    @Test
    public void testCreateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setDescription("Test Product");

        Product product = new Product();
        product.setId(1L);
        product.setDescription("Test Product");

        when(productMapper.productDTOToProduct(any(ProductDTO.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.productToProductDTO(any(Product.class))).thenReturn(productDTO);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.description").value("Test Product"));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setDescription("Test Product");

        List<Product> products = List.of(product);

        when(productRepository.findAll()).thenReturn(products);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setDescription("Test Product");

        when(productMapper.productToProductDTO(any(Product.class))).thenReturn(productDTO);

        mockMvc.perform(get("/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetProductById() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setDescription("Test Product");

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setDescription("Test Product");

        when(productMapper.productToProductDTO(any(Product.class))).thenReturn(productDTO);

        mockMvc.perform(get("/products/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.description").value("Test Product"));
    }

    @Test
    public void testGetProductByIdNotFound() throws Exception {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/1"))
            .andExpect(status().is5xxServerError());
    }

    @Test
    public void testGetProductIdsByOrderIds() throws Exception {
        List<Long> productIds = List.of(1L, 2L);

        when(productRepository.findProductIdsByOrdersIdIn(anyList())).thenReturn(productIds);

        mockMvc.perform(get("/products/by-orders")
                .param("orderIds", "1", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}