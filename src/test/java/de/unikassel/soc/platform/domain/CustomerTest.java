package de.unikassel.soc.platform.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
    }

    @Test
    void getName() {
        customer.setName("Test");
        assertEquals("Test", customer.getName());
    }

    @Test
    void getProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product.ProductBuilder().productName("Product 1").currency("Euro").description("Test product 1").price(10.5d).build());
        products.add(new Product.ProductBuilder().productName("Product 2").currency("Pound").description("Test product 2").price(1.25d).build());
        customer.setProducts(products);

        assertEquals(products, customer.getProducts());
    }
}