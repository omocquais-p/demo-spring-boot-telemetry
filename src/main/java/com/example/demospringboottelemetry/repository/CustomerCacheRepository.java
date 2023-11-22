package com.example.demospringboottelemetry.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CustomerCacheRepository extends CrudRepository<CustomerResponseDTO, UUID> {
}
