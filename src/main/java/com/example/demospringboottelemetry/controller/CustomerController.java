package com.example.demospringboottelemetry.controller;


import com.example.demospringboottelemetry.dto.CustomerDTO;
import com.example.demospringboottelemetry.repository.CustomerResponseDTO;
import com.example.demospringboottelemetry.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class CustomerController {

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @PostMapping("/customer")
  @ResponseStatus(HttpStatus.CREATED)
  public CustomerResponseDTO create(@Valid @RequestBody CustomerDTO customerDTO) {
    return customerService.create(customerDTO);
  }

  @GetMapping("/customer/{uuid}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public CustomerResponseDTO get(@PathVariable String uuid) {
    return customerService.get(UUID.fromString(uuid));
  }

  @RequestMapping("/")
  public String index() {
    return "Greetings from Spring Boot + Tanzu !!!";
  }
}
