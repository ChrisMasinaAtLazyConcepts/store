package com.example.store.mapper;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Product;


import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "orderIds", source = "orders", qualifiedByName = "orderIds")
    ProductDTO productToProductDTO(Product product);

    @Mappings({@Mapping(target = "orders", ignore = true)})
    Product productDTOToProduct(ProductDTO productDTO);

    @AfterMapping
    default void mapIdToOrder(ProductDTO productDTO) {
        if (productDTO.getOrderIds() != null) {
            Product product = productDTOToProduct(productDTO);
            List<Order> orders = product.getOrders();
            product.setOrders(orders);
        }
    } 

    @Named("orderIds")
    default Set<Long> mapOrderIds(List<Order> orders) {
        return orders.stream()
                .map(Order::getId)
                .collect(Collectors.toSet());
    }

    List<ProductDTO> productsToProductDTOs(List<Product> products);
}
