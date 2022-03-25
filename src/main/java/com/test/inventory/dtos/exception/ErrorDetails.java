package com.test.inventory.dtos.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorDetails {
    private String message;
    private String description;
    private final LocalDateTime timestamp = LocalDateTime.now();
}
