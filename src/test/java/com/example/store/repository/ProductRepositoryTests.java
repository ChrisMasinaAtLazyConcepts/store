package com.example.store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.example.store.entity.Order;
import com.example.store.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;
    
      @BeforeEach
        public void setup() {
            productRepository.deleteAllInBatch();
        }

    @Test
    public void testFindProductIdsByOrdersIdIn() {
        Product product = new Product();
        product.setDescription("Coke");
        productRepository.save(product);
        List<Product> testList =new ArrayList<>();
        testList.add(product);

        Order order = new Order();
        order.setDescription("Coke Order");
        order.setProducts(testList);
        order.setCustomer( customerRepository.findById(1L).get());
        orderRepository.save(order);

        List<Long> result = productRepository.findProductIdsByOrdersIdIn(List.of(order.getId()));
        assertEquals(1, result.size());
    }
}