package com.test.inventory.controllers;


import com.test.inventory.dtos.BigOrderDto;
import com.test.inventory.dtos.OrdersByDateAndStore;
import com.test.inventory.dtos.SoldProducts;
import com.test.inventory.dtos.exception.ApplicationException;
import com.test.inventory.dtos.exception.ErrorDetails;
import com.test.inventory.services.OrderService;
import com.test.inventory.utils.controllererrors.ControllerErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.List;

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


    @GetMapping("/orders/numberByDateAndStore")
    public ResponseEntity<List<OrdersByDateAndStore>> obtainNumberOfOrdersGroupedByStoreAndDate(){
        return ResponseEntity.ok(orderService.obtainNumberOfOrdersByDateAndStore());
    }

    @GetMapping("/orders/numberOfSoldProductsByStore")
    public ResponseEntity<List<SoldProducts>> obtainNumberOfSoldProductsByStore(){
        return ResponseEntity.ok(orderService.obtainNumberOfSoldProductsByStore());
    }


    @GetMapping(value = "/clients/{clientIdentification}/orders")/*,
            produces = "text/csv")*/
    public ResponseEntity<?> generateReportOrdersByClient(
            @PathVariable String clientIdentification,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate){

        if(startDate.isAfter(endDate))
            throw ApplicationException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .errorDetails(ErrorDetails.builder()
                            .message("Error in the dates provided")
                            .description("The init date is after the end date")
                            .build())
                    .build();

        InputStreamResource fileInputStream = orderService.obtainOrdersClientBetweenDates(
                        startDate, endDate, clientIdentification);
        String csvFileName = "people.csv";
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFileName);

        return new ResponseEntity<>(
                fileInputStream,
                headers,
                HttpStatus.OK
        );
    }

}
