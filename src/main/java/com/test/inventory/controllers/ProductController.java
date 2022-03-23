package com.test.inventory.controllers;


import com.test.inventory.dtos.ProductBasicInformation;
import com.test.inventory.dtos.exception.ApplicationException;
import com.test.inventory.dtos.exception.ErrorDetails;
import com.test.inventory.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {


    private final ProductService productService;

    public ProductController(ProductService productService) {
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

        return ResponseEntity.ok(productService.updateStock(code, stock));
    }

    @GetMapping("/test")
    public void throwException(){
        throw ApplicationException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errorDetails(ErrorDetails.builder().message("test").description("test2").build())
                .build();
    }


}
