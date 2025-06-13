package com.example.store.controller;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    /**
     * Handles requests to get all orders.
     * 
     * @return List of OrderDTO objects
     */
    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
            .stream()
            .map(orderMapper::orderToOrderDTO)
            .toList();
    }

    @GetMapping("/by-products")
    public List<Long> getOrderIdsByProductIds(@RequestParam List<Long> productIds) {
        return orderRepository.findOrderIdsByProductIds(productIds);
    }

    /**
     * Handles requests to get an order by ID.
     * 
     * @param id : ID of the order
     * @return OrderDTO object
     */
    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
            .map(orderMapper::orderToOrderDTO)
            .orElseThrow(() -> new RuntimeException("Order not found with ID " + id));
    }

    /**
     * Handles requests to create an order.
     * 
     * @param orderDTO : OrderDTO object
     * @return OrderDTO object of the created order
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@RequestBody OrderDTO orderDTO) {
        Order order = orderMapper.orderDTOToOrder(orderDTO);
        return orderMapper.orderToOrderDTO(orderRepository.save(order));
    }
}