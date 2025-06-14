package com.example.store.mapper;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {CustomerMapper.class, ProductMapper.class})
public interface OrderMapper {

    @Mappings({@Mapping(source = "customer", target = "customer"), @Mapping(source = "products", target = "products")})
    OrderDTO orderToOrderDTO(Order order);

    // @Mappings({@Mapping(target = "id", ignore = true)})
    Order orderDTOToOrder(OrderDTO orderDTO);

    List<OrderDTO> ordersToOrderDTOs(List<Order> orders);
}
