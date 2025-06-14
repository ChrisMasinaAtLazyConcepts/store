package com.example.store.mapper;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {OrderMapper.class})
public interface CustomerMapper {

    @Mappings({@Mapping(source = "orders", target = "orders")})
    CustomerDTO customerToCustomerDTO(Customer customer);

    @InheritInverseConfiguration
    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> customersToCustomerDTOs(List<Customer> customers);
}
