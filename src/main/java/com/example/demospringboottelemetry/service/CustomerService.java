package com.example.demospringboottelemetry.service;


import com.example.demospringboottelemetry.dto.CustomerDTO;
import com.example.demospringboottelemetry.repository.CustomerCacheRepository;
import com.example.demospringboottelemetry.repository.CustomerResponseDTO;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);


    private final CustomerCacheRepository customerCacheRepository;


    public CustomerService(CustomerCacheRepository customerCacheRepository) {
        this.customerCacheRepository = customerCacheRepository;
    }

    @Observed(name = "create",
            contextualName = "creating-customer",
            lowCardinalityKeyValues = {"userType", "userType1"})
    public CustomerResponseDTO create(CustomerDTO customerDTO) {

        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO(UUID.randomUUID(), customerDTO.firstName(), customerDTO.lastName(), "company");

        CustomerResponseDTO savedCustomerResponseDTO = customerCacheRepository.save(customerResponseDTO);

        LOGGER.info("New customer: {}", savedCustomerResponseDTO);

        return savedCustomerResponseDTO;
    }

    @Observed(name = "get",
            contextualName = "getting-customer",
            lowCardinalityKeyValues = {"userType", "userType2"})
    public CustomerResponseDTO get(UUID uuid) {
        LOGGER.info("Try to get CustomerResponseDTO for uuid={}", uuid);
        Optional<CustomerResponseDTO> optCustomerResponseDTO = customerCacheRepository.findById(uuid);
        if (optCustomerResponseDTO.isPresent()) {
            LOGGER.info("CustomerResponseDTO found on Cache for uuid={}", uuid);
            return optCustomerResponseDTO.get();
        } else {
            LOGGER.info("Try to get CustomerResponseDTO from database for uuid={}", uuid);
            throw new IllegalArgumentException("Customer not found");
        }
    }
}
