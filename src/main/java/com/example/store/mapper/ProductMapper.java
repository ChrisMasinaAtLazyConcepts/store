package com.example.store.mapper;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Product;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mappings({@Mapping(target = "orderIds", ignore = true)})
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

    List<ProductDTO> productsToProductDTOs(List<Product> products);
}
