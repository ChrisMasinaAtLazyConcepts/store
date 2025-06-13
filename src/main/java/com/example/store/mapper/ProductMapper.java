package com.example.store.mapper;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ProductMapper {

    @Mapping(target = "orderIds", source = "orders", qualifiedByName = "mapOrdersToIds")
    public abstract ProductDTO productToProductDTO(Product product);

    @Mapping(target = "orders", ignore = true) 
    public abstract Product productDTOToProduct(ProductDTO productDTO);

    public abstract List<ProductDTO> productsToProductDTOs(List<Product> products);
    public abstract List<Product> productDTOsToProducts(List<ProductDTO> productDTOs);

    @Named("mapOrdersToIds")
    protected Set<Long> mapOrdersToIds(List<Order> orders) {
        if (orders == null) {
            return Set.of();
        }
        return orders.stream()
                .map(Order::getId)
                .collect(Collectors.toSet());
    }

    @Named("mapProductsToDTOs")
    protected List<ProductDTO> mapProductsToDTOs(List<Product> products) {
        if (products == null) {
            return List.of();
        }
        return products.stream()
                .map(product -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setId(product.getId());
                    dto.setDescription(product.getDescription());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Named("productDTOsToProducts")
    protected List<Product> mapProductDTOsToProducts(List<ProductDTO> productDTOs) {
        if (productDTOs == null) {
            return List.of();
        }
        return productDTOs.stream()
                .map(productDTO -> {
                    Product product = new Product();
                    product.setId(productDTO.getId());
                    product.setDescription(productDTO.getDescription());
                    return product;
                })
                .collect(Collectors.toList());
    }
}