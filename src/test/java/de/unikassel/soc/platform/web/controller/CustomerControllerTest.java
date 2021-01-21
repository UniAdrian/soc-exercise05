package de.unikassel.soc.platform.web.controller;

import de.unikassel.soc.platform.services.CustomerService;
import de.unikassel.soc.platform.web.model.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomerControllerTest {

    @Mock
    CustomerService customerService;

    CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerController = new CustomerController(customerService);
    }

    @Test
    void getCustomer() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        UUID uuid = UUID.randomUUID();
        CustomerDto customerDto = new CustomerDto(uuid, "Test", null);
        when(customerService.getCustomerById(uuid)).thenReturn(customerDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customer/" + uuid))
                .andExpect(status().isOk());
    }

    @Test
    void getCustomerNotFound() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new MvcExceptionHandler())
                .build();
        UUID uuid = UUID.randomUUID();
        when(customerService.getCustomerById(uuid)).thenThrow(NoSuchElementException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customer/" + uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCustomersByName() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        UUID uuid = UUID.randomUUID();
        CustomerDto customerDto = new CustomerDto(UUID.randomUUID(), uuid.toString(), null);
        CustomerDto customerDto2 = new CustomerDto(UUID.randomUUID(), uuid.toString(), null);

        when(customerService.getCustomersByName(uuid.toString())).thenReturn(new ArrayList<>() {{
            add(customerDto);
            add(customerDto2);
        }});

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customer/").param("name", uuid.toString()))
                .andExpect(status().isOk())
                // Check application type.
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Check valid json
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                // Check that the two entries received have our random uuids.
                .andExpect(jsonPath("$[0].id").value(customerDto.getId().toString()))
                .andExpect(jsonPath("$[1].id").value(customerDto2.getId().toString()));
    }

    @Test
    void handlePostError() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        String invalidRequest = "{\"invalid\":\"json request\"}";

        // Send malformed package.
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customer/").content(invalidRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void handlePost() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        UUID uuid = UUID.randomUUID();

        final String validRequest = "{\n" +
            "    \"id\":\"" + uuid.toString() + "\",\n" +
            "    \"name\":\"To Post\",\n" +
            "    \"products\": []\n" +
            "}";

        CustomerDto customerDto = CustomerDto.builder().id(uuid).name("To Post").products(new ArrayList<>()).build();

        when(customerService.saveNewCustomer(customerDto)).thenReturn(customerDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customer/").content(validRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/customer/" + uuid.toString()));

        verify(customerService, times(1)).saveNewCustomer(customerDto);
    }
}