package com.example.store.controller;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.OrderRepository;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order Controller")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @GetMapping
    @Operation(summary = "Get all orders")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    public ResponseEntity<?> getAllOrders() {
        try {
            List<OrderDTO> orders = orderRepository.findAll().stream()
                    .map(orderMapper::orderToOrderDTO)
                    .toList();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get orders");
        }
    }

    @GetMapping("/by-products")
    @Operation(summary = "Get order IDs by product IDs")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    public ResponseEntity<?> getOrderIdsByProductIds(@RequestParam List<Long> productIds) {
        try {
            List<Long> orderIds = orderRepository.findOrderIdsByProductIds(productIds);
            return ResponseEntity.ok(orderIds);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to get orders please confim product ids are valid" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            Order order = orderRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found with ID " + id));
            return ResponseEntity.ok(orderMapper.orderToOrderDTO(order));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create order")
    @ApiResponses(value = {@ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "400")})
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        try {
            Order order = orderMapper.orderDTOToOrder(orderDTO);
            Order savedOrder = orderRepository.save(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.orderToOrderDTO(savedOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body("Invalid request body");
    }
}
