package com.example.demospringboottelemetry.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@RedisHash("customer")
public record CustomerResponseDTO(@Id UUID uuid, String firstName, String name, String company) {
}
