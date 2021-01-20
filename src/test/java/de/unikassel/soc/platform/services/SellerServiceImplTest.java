package de.unikassel.soc.platform.services;

import de.unikassel.soc.platform.domain.Product;
import de.unikassel.soc.platform.domain.Seller;
import de.unikassel.soc.platform.repos.ProductRepo;
import de.unikassel.soc.platform.repos.SellerRepo;
import de.unikassel.soc.platform.web.mappers.SellerMapper;
import de.unikassel.soc.platform.web.mappers.SellerMapperImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SellerServiceImplTest {

    static SellerMapper mapper;
    SellerServiceImpl sellerService;

    @Mock
    SellerRepo repo;

    @BeforeAll
    static void beforeAll() { mapper = new SellerMapperImpl(); }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sellerService = new SellerServiceImpl(repo, mapper);
    }

    @Test
    void deleteById() {
        // Test once to delete an "existing" seller and once for an "none existing" seller.

        UUID uuidNoExist = UUID.randomUUID();
        UUID uuidExist = UUID.randomUUID();

        when(repo.findById(uuidNoExist)).thenReturn(Optional.empty());
        when(repo.findById(uuidExist)).thenReturn(Optional.of(
                Seller.builder()
                .id(uuidExist)
                .name("Test")
                .products(new ArrayList<>())
                .build())
        );

        assertDoesNotThrow(() -> sellerService.deleteById(uuidNoExist));
        assertDoesNotThrow(() -> sellerService.deleteById(uuidExist));
    }
}