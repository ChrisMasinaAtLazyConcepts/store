package com.example.store.controller;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.mapper.OrderMapper;
import com.example.store.service.OrderService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;



@RestController
@RequestMapping("/store/orders")
@RequiredArgsConstructor
@Tag(name = "Order Controller")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    @Operation(summary = "Create order")
    @ApiResponses(value = {@ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "400")})
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        if(orderDTO.getId() != null){
            return ResponseEntity.badRequest().body("New order cannot have Id");
        }
        try {
            Order order = orderMapper.orderDTOToOrder(orderDTO);
            Order savedOrder = orderService.createOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Get all orders")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    public ResponseEntity<?> getAllOrders() {
       try {
            List<OrderDTO> orders = orderService.getAllOrders().stream()
                    .map(orderMapper::orderToOrderDTO)
                    .toList();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get orders: " + e.getMessage());
        }
    }

    
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
           Order order = orderService.getOrderById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with ID " + id));
            return ResponseEntity.ok(orderMapper.orderToOrderDTO(order));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
