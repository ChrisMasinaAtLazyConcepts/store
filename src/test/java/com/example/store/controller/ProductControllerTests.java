package com.example.store.controller;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.repository.OrderRepository;
import com.example.store.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private OrderRepository orderRepository;
    

    @Test
    public void testCreateProduct() throws Exception {
       Product product = new Product();
       product.setDescription("Oats");

       ProductDTO productDTO = new ProductDTO();
       productDTO.setId(1L);

        when(productService.createProduct(any(ProductDTO.class))).thenReturn(product);

        mockMvc.perform(post("/store/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetAllProducts() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setDescription("Test Product");
        List<ProductDTO> products = List.of(productDTO);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/store/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }


    @Test
    public void testGetProductByIdNotFound() throws Exception {
        when(productService.getProductById(anyLong())).thenReturn(null);
        mockMvc.perform(get("/store/products/100")).andExpect(status().isNotFound());
    }

    @Test
    public void testGetProductIdsByOrderIds() throws Exception {
        List<Long> productIds = List.of(1L, 2L);

        when(productService.getProductIdsByOrderIds(anyList())).thenReturn(productIds);

        mockMvc.perform(get("/store/products/by-orders").param("orderIds", "1", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testCreateProductNull() throws Exception {
        mockMvc.perform(post("/store/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetProductIdsByOrderIdsNull() throws Exception {
        mockMvc.perform(get("/store/products/by-orders")).andExpect(status().isBadRequest());
    }

    @Test
    public void testGetProductById()throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setDescription("Test Product");

        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/store/products?/id=1"))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
