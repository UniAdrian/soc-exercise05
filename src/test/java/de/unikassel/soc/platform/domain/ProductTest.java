package de.unikassel.soc.platform.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
    }

    @Test
    void getDescription() {
        product.setDescription("Test description.");
        assertEquals("Test description.", product.getDescription());
    }

    @Test
    void getPrice() {
        double price = Math.random();
        product.setPrice(price);
        assertEquals(price, product.getPrice());
    }
}