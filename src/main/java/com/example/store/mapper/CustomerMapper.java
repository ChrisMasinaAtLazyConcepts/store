package com.example.store.mapper;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mappings({@Mapping(target = "orders", ignore = true)})
    CustomerDTO customerToCustomerDTO(Customer customer);

    default void mapOrders(Customer customer, CustomerDTO customerDTO) {
        OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
        customerDTO.setOrders(orderMapper.ordersToOrderDTOs(customer.getOrders()));
    }

    @InheritInverseConfiguration
    @Mappings({@Mapping(target = "orders", ignore = true)})
    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> customersToCustomerDTOs(List<Customer> customers);
}
