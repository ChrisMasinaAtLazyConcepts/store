package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createCustomer(@RequestBody Customer customer) {
        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }

    /**
     *Handles requests to get an order by customer by ID.
     * 
     * @return List<CustomerDTO>
     */
    @Cacheable("customers")
    @GetMapping
    public List<CustomerDTO> getAllCustomers(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (page != 0 && size != 0){
            return customerRepository.findAll(pageable)
                .stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
        }
        return customerMapper.customersToCustomerDTOs(customerRepository.findAll(pageable).getContent());
    }

    /**
     *Handles requests to get an order by customer by ID.
     * 
     * @param name : query string , partial or customer name
     * @param name :  page int
     * @param name :  size int 
     * @return OrderDTO object
     */

    @GetMapping("/search")
    public List<CustomerDTO> searchCustomers(@RequestParam(required = false) String query,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (query != null && !query.isEmpty()) {
            return customerRepository.findByNameContainingIgnoreCase(query, pageable)
                .stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
        }
        return customerMapper.customersToCustomerDTOs(customerRepository.findAll(pageable).getContent());
    }
}
