package com.example.store.controller;

import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import com.example.store.mapper.OrderMapper;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.OrderRepository;
import com.example.store.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.store.dto.OrderCustomerDTO;
import com.example.store.dto.OrderDTO;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(OrderController.class)
public class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private OrderMapper orderMapper;

    @MockitoBean
    private ProductMapper productMapper;
    

    @Test
    void testGetAllOrders() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setDescription("Test Order");

        List<Order> orders = List.of(order);

        when(orderService.getAllOrders()).thenReturn(orders);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setDescription("Test Order");

        when(orderMapper.orderToOrderDTO(any(Order.class))).thenReturn(orderDTO);

        mockMvc.perform(get("/orders"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetOrderById() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");

        Product product = new Product();
        product.setId(1L);
        product.setDescription("Test Product");

        Order order = new Order();
        order.setId(1L);
        order.setDescription("Test Order");
        order.setCustomer(customer);
        order.setProducts(List.of(product));

        when(orderService.findById(anyLong())).thenReturn(order);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setDescription("Test Order");
        orderDTO.setId(1L);

        when(orderMapper.orderToOrderDTO(any(Order.class))).thenReturn(orderDTO);

        mockMvc.perform(get("/orders/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.description").value("Test Order"));
    }

    @Test
    void testGetOrderByIdNotFound() throws Exception {
        when(orderService.findById(anyLong()));

        mockMvc.perform(get("/orders/1"))
            .andExpect(status().is5xxServerError());
    }

    @Test
    void testCreateOrder() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");

        Product product = new Product();
        product.setId(1L);
        product.setDescription("Test Product");

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setDescription("Test Order");
        orderDTO.setCustomer(new OrderCustomerDTO(1L, "Customer A"));
        orderDTO.setProducts(List.of(productMapper.productToProductDTO(product)));

        Order order = new Order();
        order.setId(1L);
        order.setDescription("Test Order");
        order.setCustomer(customer);
        order.setProducts(List.of(product));

        when(orderMapper.orderDTOToOrder(any(OrderDTO.class))).thenReturn(order);
        when(orderService.createOrder(any(Order.class))).thenReturn(order);
        when(orderMapper.orderToOrderDTO(any(Order.class))).thenReturn(orderDTO);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(orderDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.description").value("Test Order"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}