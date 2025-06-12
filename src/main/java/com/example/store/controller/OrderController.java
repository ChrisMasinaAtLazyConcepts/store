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
     *Handles requests to get an order by customer by ID.
     * 
     * @param customerId : customerId of the order 
     * @return OrderDTO object
     */
    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAllWithProducts()
            .stream()
            .map(orderMapper::orderToOrderDTO)
            .toList();
    }

    /**
     *Handles requests to create an order
     * 
     * @param order : Order object 
     * @return OrderDTO object of the created order
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@RequestBody OrderDTO orderDTO) {
        Order order = orderMapper.orderDTOToOrder(orderDTO);
        return orderMapper.orderToOrderDTO(orderRepository.save(order));
    }
}
