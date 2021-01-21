package de.unikassel.soc.platform.web.controller;

import de.unikassel.soc.platform.services.ProductService;
import de.unikassel.soc.platform.web.model.CustomerDto;
import de.unikassel.soc.platform.web.model.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

class ProductControllerTest {

    @Mock
    ProductService productService;

    ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productController = new ProductController(productService);
    }

    @Test
    void getProductsByPriceRange() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        UUID uuid = UUID.randomUUID();

        ProductDto productDto1 = new ProductDto(uuid, "Test1", "In Range.", 10.0, "Euro");
        ProductDto productDto2 = new ProductDto(uuid, "Test2", "In Range.", 15.0, "Euro");

        List<ProductDto> asList = new ArrayList<>() {{
            add(productDto1);
            add(productDto2);
        }};

        when(productService.getProductsByPriceBetween(10.0, 20.0)).thenReturn(asList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product/").param("from", "10.0").param("to", "20.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productName").value("Test1"))
                .andExpect(jsonPath("$[0].price").value("10.0"))
                .andExpect(jsonPath("$[1].productName").value("Test2"))
                .andExpect(jsonPath("$[1].price").value("15.0"));
    }

}