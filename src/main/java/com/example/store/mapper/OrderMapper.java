package com.example.store.mapper;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {CustomerMapper.class, ProductMapper.class})
public interface OrderMapper {

    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "products", source = "products")
    OrderDTO orderToOrderDTO(Order order);

    @InheritInverseConfiguration
    Order orderDTOToOrder(OrderDTO orderDTO);

    List<OrderDTO> ordersToOrderDTOs(List<Order> orders);
}