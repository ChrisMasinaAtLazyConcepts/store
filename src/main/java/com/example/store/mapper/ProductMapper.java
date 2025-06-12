package com.example.store.mapper;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    
    @Mapping(target = "orderIds", source = "orders", qualifiedByName = "mapOrdersToIds")
    ProductDTO productToProductDTO(Product product);
    
    @Named("mapOrdersToIds")
    default Set<Long> mapOrdersToIds(Set<Order> orders) {
        return orders.stream()
            .map(Order::getId)
            .collect(Collectors.toSet());
    }
    
    Product productDTOToProduct(ProductDTO productDTO);
}