package com.test.inventory.controllers;


import com.test.inventory.dtos.BigOrderDto;
import com.test.inventory.dtos.exception.ApplicationException;
import com.test.inventory.services.OrderService;
import com.test.inventory.utils.controllererrors.ControllerErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody  BigOrderDto bigOrderDto, BindingResult result){
        System.out.println("------------ERRORS  " + result.hasErrors());
        if(result.hasErrors()){
            throw  ApplicationException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .errorDetails(ControllerErrors.resultErrorsToErrorDetails(result))
                    .build();
        }

        return ResponseEntity.ok(orderService.createOrderResume(bigOrderDto));

    }


}
