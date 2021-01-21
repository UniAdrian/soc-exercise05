package de.unikassel.soc.platform.repos;

import de.unikassel.soc.platform.domain.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepoIntegration {

    @Autowired
    CustomerRepo customerRepo;

    @Test
    @Transactional
    @DirtiesContext
    void findByName() {
        customerRepo.saveAll(List.of(
                new Customer(UUID.randomUUID(), "Hans", null),
                new Customer(UUID.randomUUID(), "Hans", null),
                new Customer(UUID.randomUUID(), "Tanja", null),
                new Customer(UUID.randomUUID(), "Regina", null)
        ));

        List<Customer> customers = customerRepo.findByName("Hans");
        assertEquals(2, customers.size());
    }

    @Test
    @Transactional
    @DirtiesContext
    void findById() {
        // Insert a new Customer, the database will automatically assign a new UUID.
        Customer storedCustomer = customerRepo.save(new Customer(UUID.randomUUID(), "Hans", null));

        Optional<Customer> customer = customerRepo.findById(storedCustomer.getId());
        assertTrue(customer.isPresent());
        assertEquals(customer.get(), storedCustomer);
    }

    @Test
    void findInitEmpty() {
        List<Customer> customers = customerRepo.findAll();
        assertEquals(0, customers.size());
    }
}