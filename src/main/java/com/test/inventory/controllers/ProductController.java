package com.test.inventory.controllers;


import com.test.inventory.dtos.ProductBasicInformation;
import com.test.inventory.dtos.exception.ApplicationException;
import com.test.inventory.dtos.exception.ErrorDetails;
import com.test.inventory.entities.Order;
import com.test.inventory.entities.OrderResume;
import com.test.inventory.services.ProductService;
import com.test.inventory.services.implementation.ProductServiceImpl;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {


    private final ProductService productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductBasicInformation>> obtainAllProducts(){
        return ResponseEntity.ok(productService.obtainAllProducts());
    }

    @PatchMapping("/products/{code}/stock")
    public ResponseEntity<String> updateProductStock(
            @PathVariable String code,
            @RequestParam  Integer stock){
        if(stock <= 0)
            throw ApplicationException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .errorDetails(ErrorDetails.builder()
                            .message("Invalid stock")
                            .description("The Stock has to be greater than 0")
                            .build())
                    .build();

        boolean updated = productService.updateStock(code, stock);
        if(!updated)
            return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }



    @GetMapping(value = "/test")
    public ResponseEntity<OrderResume> throwException(){
        OrderResume orderResume = new OrderResume();
        addItem(orderResume);
        return ResponseEntity.ok(orderResume);
    }

    private void addItem(OrderResume orderResume){
        addItem2(orderResume);
    }

    private void addItem2(OrderResume orderResume){
        orderResume.addOrder(Order.builder().totalPrice(222d).build());
    }



}
