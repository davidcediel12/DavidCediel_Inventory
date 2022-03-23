package com.test.inventory.controllers;


import com.test.inventory.dtos.ProductBasicInformation;
import com.test.inventory.dtos.ProductsDto;
import com.test.inventory.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
