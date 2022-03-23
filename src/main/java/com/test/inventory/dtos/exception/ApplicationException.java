package com.test.inventory.dtos.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
public class ApplicationException extends RuntimeException {
    ErrorDetails errorDetails;
    HttpStatus status;
}
