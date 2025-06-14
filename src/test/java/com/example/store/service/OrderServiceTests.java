package com.example.store.service;

import com.example.store.entity.Order;
import com.example.store.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testCreateOrder() {
        Order order = new Order();
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.createOrder(order);

        assertEquals(order, result);
    }

    @Test
    void testGetOrderByCustomerId() {
        List<Order> orders = List.of(new Order(), new Order());
        when(orderRepository.findByCustomerId(1L)).thenReturn(orders);

        List<Order> result = orderService.getOrderByCustomerId("1");

        assertEquals(orders, result);
    }

    @Test
    void testGetAllOrders() {
        List<Order> orders = List.of(new Order(), new Order());
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();
        assertEquals(orders, result);
    }

    @Test
    void testFindById() {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.findById(1L);

        assertEquals(order, result);
    }

    @Test
    void testGetOrdersByIds() {
        List<Order> orders = List.of(new Order(), new Order());
        Set<Long> orderIds = Set.of(1L, 2L);
        when(orderRepository.findByIdIn(orderIds)).thenReturn(orders);

        List<Order> result = orderService.getOrdersByIds(orderIds);

        assertEquals(orders, result);
    }
}