package com.example.demospringboottelemetry.controller;

import com.example.demospringboottelemetry.configuration.MyContainersConfiguration;
import com.example.demospringboottelemetry.dto.CustomerDTO;
import com.example.demospringboottelemetry.repository.CustomerCacheRepository;
import com.example.demospringboottelemetry.repository.CustomerResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@Import(MyContainersConfiguration.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerCacheRepository customerCacheRepository;

    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectWriter jsonWriter = mapper.writerFor(CustomerDTO.class);

    @BeforeEach
    public void reset() {
        customerCacheRepository.deleteAll();
    }

    @DisplayName("Given a customer (firstname and a name), it should create a new customer")
    @Test
    public void create() throws Exception {
        long before = customerCacheRepository.count();
        assertEquals(0, before);

        CustomerDTO customerDTO = new CustomerDTO("John", "Smith1");

        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWriter.writeValueAsString(customerDTO))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.name").value("Smith1"));

        long after = customerCacheRepository.count();
        assertEquals(1, after);

    }

    @DisplayName("Given a customer without firstname and name, it should throw a bad request exception")
    @Test
    public void validation() throws Exception {
        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWriter.writeValueAsString(new CustomerDTO(null, null)))
                )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Given an ID from an existing customer , it should return the customer")
    @Test
    public void getCustomer() throws Exception {

        CustomerResponseDTO customer = new CustomerResponseDTO(UUID.randomUUID(), "Bob", "Smith", "MyCompany");

        CustomerResponseDTO customerSaved = customerCacheRepository.save(customer);

        String uuid = customerSaved.uuid().toString();

        mockMvc.perform(get("/customer/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.firstName").value(customer.firstName()))
                .andExpect(jsonPath("$.name").value(customer.name()))
                .andExpect(jsonPath("$.company").value(customer.company()))
                .andExpect(jsonPath("$.uuid").isNotEmpty());
    }

    @Test
    void index() {
    }
}