package com.example.store.integration;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;

    @AfterEach
    public void cleanup() {
        Mockito.reset();
    }

    @BeforeEach
    public void setup() {
        productRepository.deleteAllInBatch();
        testProduct = new Product();
        testProduct.setDescription("Test Product");
        testProduct = productRepository.save(testProduct);
    }

  @Test
    public void createProductReturnCreatedProduct() throws Exception {
        productRepository.deleteAll();
        ProductDTO newProduct = new ProductDTO();
        newProduct.setDescription("New Test Product");

        mockMvc.perform(post("/store/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("New Test Product"))
                .andExpect(jsonPath("$.id").exists());

        assertEquals(1, productRepository.count());
    }


    @Test
    public void getProductByIdReturnProduct() throws Exception {
        ProductDTO newProduct = new ProductDTO();
        newProduct.setDescription("Test Product");

        MvcResult result = mockMvc.perform(post("/store/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ProductDTO createdProduct = objectMapper.readValue(responseBody, ProductDTO.class);

        mockMvc.perform(get("/store/products/{id}", createdProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdProduct.getId()))
                .andExpect(jsonPath("$.description").value("Test Product"));
    }

    @Test
    public void getProductByIdNotExistsReturnNotFound() throws Exception {
        long nonExistentId = 999L;
        MvcResult result = mockMvc.perform(get("/store/products/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void getProductIdsByOrderIdsReturnProductIds() throws Exception {
        mockMvc.perform(get("/store/products/by-orders")
                .param("orderIds", "1", "2"))
                .andExpect(status().isOk());
    }
}