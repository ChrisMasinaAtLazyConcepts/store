package com.example.store.mapper;

import com.example.store.dto.OrderCustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Customer;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;


public abstract class OrderMapper {

    @Autowired
    private ProductMapper productMapper;

    public OrderDTO orderToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setDescription(order.getDescription());
        orderDTO.setCustomer(new OrderCustomerDTO(order.getCustomer().getId(),order.getCustomer().getName()));
        orderDTO.setProducts(productMapper.mapProductsToDTOs(order.getProducts()));
        return orderDTO;
    }

    public Order orderDTOToOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setDescription(orderDTO.getDescription());
        
        if (orderDTO.getCustomer() != null) {
            Customer customer = new Customer();
            customer.setId(orderDTO.getCustomer().getId());
            customer.setName(orderDTO.getCustomer().getName());
            order.setCustomer(customer);
        }
        order.setProducts(productMapper.productDTOsToProducts(orderDTO.getProducts()));
        return order;
    }
    public List<OrderDTO> ordersToOrderDTOs(List<Order> orders) {
        return orders.stream()
                .map(this::orderToOrderDTO)
                .collect(Collectors.toList());
    }
}