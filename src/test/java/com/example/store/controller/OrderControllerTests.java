package com.example.store.controller;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import com.example.store.security.JwtUserDetailsService;
import com.example.store.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(OrderController.class)
public class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private OrderService orderService;
    
    @MockitoBean
    private ProductRepository productRepository;

    @MockitoBean
    private OrderMapper orderMapper;

    @MockitoBean
    private JwtUserDetailsService jwtUserDetailsService;

    @AfterEach
    public void cleanup() {
        Mockito.reset(orderRepository, orderMapper);
    }

    @Test
    public void testGetAllOrders() throws Exception {
        Order order = new Order();
        order.setDescription("Test Order");

        List<Order> orders = List.of(order);

        when(orderService.getAllOrders()).thenReturn(orders);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setDescription("Test Order");

        when(orderMapper.ordersToOrderDTOs(anyList())).thenReturn(List.of(orderDTO));

        mockMvc.perform(get("/store/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetOrderById() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setDescription("Test Order");

        when(orderService.getOrderById(anyLong())).thenReturn(Optional.of(order));

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setDescription("Test Order");

        when(orderMapper.orderToOrderDTO(any(Order.class))).thenReturn(orderDTO);

        mockMvc.perform(get("/store/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Test Order"));
    }

    @Test
    public void testGetOrderByIdNotFound() throws Exception {
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/store/orders/1")).andExpect(status().isNotFound());
    }

   @Test
    public void testCreateOrder() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setDescription("Test Order");

        Order order = new Order();
        order.setId(1L);
        order.setDescription("Test Order");

        OrderDTO savedOrderDTO = new OrderDTO();
        savedOrderDTO.setId(1L);
        savedOrderDTO.setDescription("Test Order");

        when(orderMapper.orderDTOToOrder(any(OrderDTO.class))).thenReturn(order);
        when(orderService.createOrder(any(Order.class))).thenReturn(order);
        when(orderMapper.orderToOrderDTO(any(Order.class))).thenReturn(savedOrderDTO);

        mockMvc.perform(post("/store/orders")
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
