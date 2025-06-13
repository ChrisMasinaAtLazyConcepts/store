package com.example.store.mapper;

import com.example.store.dto.CustomerDTO;
import com.example.store.dto.CustomerOrderDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.entity.Customer;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CustomerMapper {

    @Autowired
    private OrderMapper orderMapper;

    public CustomerDTO customerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        List<OrderDTO> orderDTOs= orderMapper.ordersToOrderDTOs(customer.getOrders());
        customerDTO.setOrders(mapOrdersToCustomerOrders(orderDTOs));
        return customerDTO;
    }

List<CustomerOrderDTO> mapOrdersToCustomerOrders(List<OrderDTO> orders) {
    if (orders == null) {
        return List.of();
    }
    return orders.stream()
    .map(order -> {
        CustomerOrderDTO customerOrderDTO = new CustomerOrderDTO();
        customerOrderDTO.setId(order.getId());
        customerOrderDTO.setDescription(order.getDescription());
        return customerOrderDTO;
    })
    .collect(Collectors.toList());
    }

    public Customer customerDTOToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setId(customerDTO.getId());
        customer.setName(customerDTO.getName());
        List<OrderDTO> orderDTOs= orderMapper.ordersToOrderDTOs(customer.getOrders());
        customerDTO.setOrders(mapOrdersToCustomerOrders(orderDTOs));
        return customer;
    }

    public List<CustomerDTO> customersToCustomerDTOs(List<Customer> customers) {
        return customers.stream()
                .map(this::customerToCustomerDTO)
                .collect(Collectors.toList());
    }
}