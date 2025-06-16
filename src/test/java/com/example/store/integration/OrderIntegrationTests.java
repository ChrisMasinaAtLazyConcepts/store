package com.example.store.integration;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import com.example.store.repository.CustomerRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer testCustomer;
    
    private Order testOrder;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        orderRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();
        testCustomer = new Customer();
        testCustomer.setName("Test Customer");
        testCustomer = customerRepository.save(testCustomer);

        testOrder = new Order();
        testOrder.setDescription("Test Order");
        testOrder.setCustomer(testCustomer);
        
        Product product = new Product();
        product.setDescription("Test Product");
        productRepository.save(product);
        testOrder = orderRepository.save(testOrder);
    }


    @Test
    public void createOrderReturnCreatedOrder() throws Exception {
        OrderDTO newOrder = new OrderDTO();
        newOrder.setDescription("New Test Order");

        mockMvc.perform(post("/store/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("New Test Order"));
    }

    @Test
    public void createOrderWithInvalidDataReturnBadRequest() throws Exception {
        OrderDTO newOrder = new OrderDTO();
        newOrder.setId(1L); // we will explicitly set id

        mockMvc.perform(post("/store/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllOrdersReturnListOfOrders() throws Exception {
        mockMvc.perform(get("/store/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void getOrderByIdReturnOrder() throws Exception {
        mockMvc.perform(get("/store/orders/{id}", testOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testOrder.getId()))
                .andExpect(jsonPath("$.description").value("Test Order"));
    }

}