package de.unikassel.soc.platform.repos;

import de.unikassel.soc.platform.domain.Seller;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class SellerRepoIntegration {

    @Autowired
    SellerRepo sellerRepo;

    @Test
    @Order(0)
    void findInitEmpty() {
        List<Seller> sellers = sellerRepo.findAll();
        assertEquals(0, sellers.size());
    }

    @Test
    @Transactional
    @DirtiesContext
    @Order(1)
    void deleteById() {
        Seller toDelete = sellerRepo.save(new Seller(UUID.randomUUID(), "Delete me, please! T_T", new ArrayList<>()));

        sellerRepo.deleteById(toDelete.getId());

        Optional<Seller> shouldNotExist = sellerRepo.findById(toDelete.getId());
        assertTrue(shouldNotExist.isEmpty());
    }
}