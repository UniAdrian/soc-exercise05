package de.unikassel.soc.platform.repos;

import de.unikassel.soc.platform.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ProductRepoIntegration {

    @Autowired
    ProductRepo productRepo;

    @Test
    @Transactional
    @DirtiesContext
    void findByPriceBetween() {
        List<Product> toStore =  new ArrayList<>();

        final int numElements = 1000;
        final double maxValue = 100.0;
        final double from = 30.0;
        final double to = 60.0;

        for (int i=0; i<numElements; ++i) {
            toStore.add(new Product(UUID.randomUUID(), "Test product 1", "A generic test project", Math.random() * maxValue, "Euro"));
        }
        productRepo.saveAll(toStore);

        long expected = toStore.stream().filter(e -> e.getPrice() >= 40.0 && e.getPrice() <= 60.0).count();

        List<Product> inBetween = productRepo.findByPriceBetween(40.0, 60.0);
        assertEquals(expected, inBetween.size());
    }

    @Test
    void findInitEmpty() {
        List<Product> products = productRepo.findAll();
        assertEquals(0, products.size());
    }
}