package com.test.inventory.utils.controllererrors;

import com.test.inventory.dtos.exception.ErrorDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ControllerErrors {

    public static ErrorDetails resultErrorsToErrorDetails(BindingResult result){
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message("Wrong parameters in the request body")
                .description("")
                .build();
        for(FieldError e : result.getFieldErrors())
            errorDetails.setDescription(errorDetails.getDescription() +
                    "Field: " +  e.getField() + " " + e.getDefaultMessage() +
                    System.lineSeparator());

        return errorDetails;
    }
}
