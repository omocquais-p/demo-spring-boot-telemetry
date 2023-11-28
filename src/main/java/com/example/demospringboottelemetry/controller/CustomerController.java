package com.example.demospringboottelemetry.controller;


import com.example.demospringboottelemetry.dto.CustomerDTO;
import com.example.demospringboottelemetry.repository.CustomerResponseDTO;
import com.example.demospringboottelemetry.service.CustomerService;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class CustomerController {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @PostMapping("/customer")
  @ResponseStatus(HttpStatus.CREATED)
  @Timed(value = "creating.time", description = "Time taken to create customer")
  public CustomerResponseDTO create(@Valid @RequestBody CustomerDTO customerDTO) {
    LOGGER.info("Create customer request {}", customerDTO);
    return customerService.create(customerDTO);
  }

  @GetMapping("/customer/{uuid}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public CustomerResponseDTO get(@PathVariable String uuid) {
    LOGGER.info("Get customer by uuid {}", uuid);
    return customerService.get(UUID.fromString(uuid));
  }

  @RequestMapping("/")
  public String index() {
    return "Greetings from Spring Boot + Tanzu !!!";
  }
}
