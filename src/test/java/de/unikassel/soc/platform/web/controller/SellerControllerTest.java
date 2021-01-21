package de.unikassel.soc.platform.web.controller;

import de.unikassel.soc.platform.services.SellerService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SellerControllerTest {

    @Mock
    SellerService sellerService;

    SellerController sellerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sellerController = new SellerController(sellerService);
    }

    @Test
    void deleteById() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(sellerController).build();
        UUID uuid = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/seller/" + uuid.toString()))
                .andExpect(status().isOk());

        // Expect that sellerService.deleteBy is called exactly once with the given uuid.
        verify(sellerService, times(1)).deleteById(uuid);
    }
}