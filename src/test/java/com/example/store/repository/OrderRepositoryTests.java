package com.example.store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTests {

    @Autowired
     OrderRepository orderRepository;

    @Autowired
     ProductRepository productRepository;

    @Autowired
    CustomerRepository customerRepository;

    @AfterEach
    public void cleanup() {
        Mockito.reset();
    }


    @BeforeEach
    public void setup() {
        orderRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();
    }

    @Test
    public void testFindOrderIdsByProductIds() {
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customerRepository.save(customer);

        Product product = new Product();
        productRepository.save(product);
        List<Product>testList= new ArrayList<>();
        testList.add(product);
        Order order = new Order();
        order.setDescription("Test Order");
        order.setProducts(testList);
        order.setCustomer(customer);
        orderRepository.save(order);

        List<Long> result = orderRepository.findOrderIdsByProductIds(List.of(product.getId()));
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByCustomerId() {
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customerRepository.save(customer);

        Order order = new Order();
        order.setDescription("Test Order");
        order.setCustomer(customer);
        orderRepository.save(order);

        List<Order> result = orderRepository.findByCustomerId(customer.getId());
        assertEquals(1, result.size());
    }

    @Test
    public void testFindAll() {
        
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customerRepository.save(customer);

        Order order1 = new Order();
        order1.setDescription("Testing");
        order1.setCustomer( customer);
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setCustomer( customer);
        order2.setDescription("Testing2");
        orderRepository.save(order2);

        List<Order> result = orderRepository.findAll();
        assertEquals(2, result.size());
    }
}