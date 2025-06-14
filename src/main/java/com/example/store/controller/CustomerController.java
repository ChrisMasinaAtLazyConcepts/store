package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Controller", description = "Customer Controller API")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @PostMapping
    @Operation(summary = "Create customer")
    @ApiResponses(value = {@ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "400")})
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer) {
        try {
            Customer savedCustomer = customerRepository.save(customer);
            CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(savedCustomer);
            return ResponseEntity.status(HttpStatus.CREATED).body(customerDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create customer");
        }
    }

    @GetMapping
    @Operation(summary = "Get all customers")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @Cacheable("customers")
    public ResponseEntity<?> getAllCustomers(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<CustomerDTO> customers = customerRepository.findAll(pageable).stream()
                    .map(customerMapper::customerToCustomerDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve customers");
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    public ResponseEntity<?> searchCustomers(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<CustomerDTO> customers;
            if (query != null) {
                customers = customerRepository.findByNameContainingIgnoreCase(query, pageable).stream()
                        .map(customerMapper::customerToCustomerDTO)
                        .collect(Collectors.toList());
            } else {
                customers = customerMapper.customersToCustomerDTOs(
                        customerRepository.findAll(pageable).getContent());
            }
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve customers");
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> errorMessage
                .append(error.getField())
                .append(" ")
                .append(error.getDefaultMessage())
                .append(". "));
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }
}
