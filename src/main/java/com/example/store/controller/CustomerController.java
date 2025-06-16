package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.service.CustomerService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/store/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Controller", description = "Customer Controller API")
public class CustomerController {

    @Autowired
    private  CustomerService customerService;

    @Autowired
    private  CustomerMapper customerMapper;

    @PostMapping
    @Operation(summary = "Create a new customer")
    @ApiResponses(value = {@ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "400")})
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        try {
            Customer customer = customerMapper.customerDTOToCustomer(customerDTO);
            Customer createdCustomer = customerService.createCustomer(customer);
            CustomerDTO createdCustomerDTO = customerMapper.customerToCustomerDTO(createdCustomer);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomerDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create customer");
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        try {
            Customer customer = customerService.getCustomer(id);
            if(customer!=null){
                CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
                return ResponseEntity.ok(customerDTO);
            }
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to retrieve customer"); 
        } catch (NoSuchElementException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to retrieve customer");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve customer");
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
            List<CustomerDTO> customers = customerService.getAllCustomers(pageable).stream()
                    .map(customerMapper::customerToCustomerDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve customers");
        }
    }

   @GetMapping("/search")
    @Operation(summary = "Search customers")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    public ResponseEntity<?> searchCustomers( @RequestParam(required = false) String query,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<CustomerDTO> customers;
            
            if (query != null) {
                customers = customerService.searchCustomer(query, pageable).stream()
                        .map(customerMapper::customerToCustomerDTO)
                        .collect(Collectors.toList());
                    if (customers.isEmpty()) {
                        return ResponseEntity.ok(Collections.emptyList());
                    }
            } else {
                customers = customerMapper.customersToCustomerDTOs(
                        customerService.getAllCustomers(pageable).getContent());
            }
            if (customers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No customers found");
            }
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve customers");
        }
    }
}
