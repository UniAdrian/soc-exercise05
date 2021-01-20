package de.unikassel.soc.platform.services;

import de.unikassel.soc.platform.domain.Product;
import de.unikassel.soc.platform.repos.ProductRepo;
import de.unikassel.soc.platform.web.mappers.CustomerMapperImpl;
import de.unikassel.soc.platform.web.mappers.ProductMapper;
import de.unikassel.soc.platform.web.mappers.ProductMapperImpl;
import de.unikassel.soc.platform.web.model.ProductDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    static ProductMapper mapper;
    ProductServiceImpl productService;

    @Mock
    ProductRepo repo;

    @BeforeAll
    static void beforeAll() {
        mapper = new ProductMapperImpl();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImpl(repo, mapper);
    }

    @Test
    void getProductsByPriceBetween() {
        // How many random products to insert and filter.
        final int productAmount = 1000;

        Product[] products = new Product[productAmount];

        // Set products in price range 1-1k
        for (int i = 0; i < productAmount; i++) {
            products[i] = Product.builder().price(Math.random() * 1000 + 1).build();
        }

        // Filter all products between 300 and 600 price units.
        List<Product> productsBetween = Arrays.stream(products).filter(e -> e.getPrice() >= 300.0 && e.getPrice() <= 600.0).collect(Collectors.toList());
        List<ProductDto> productDtoBetween = productsBetween.stream().map(p -> mapper.productToProductDto(p)).collect(Collectors.toList());

        when(repo.findByPriceBetween(300.0, 600.0)).thenReturn(productsBetween);
        assertEquals(productDtoBetween, productService.getProductsByPriceBetween(300.0, 600.0));
    }
}