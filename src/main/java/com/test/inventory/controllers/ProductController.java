package com.test.inventory.controllers;


import com.test.inventory.dtos.ProductBasicInformation;
import com.test.inventory.dtos.exception.ApplicationException;
import com.test.inventory.dtos.exception.ErrorDetails;
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



    @GetMapping(value = "/test",  produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> throwException(){
        File fileImageClient1 = new File("src/main/resources/ClientProfileImages/client1.jpg");
        byte[] fileContent = new byte[]{};
        try {
            fileContent = Files.readAllBytes(fileImageClient1.toPath());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return ResponseEntity.ok(new ByteArrayResource(fileContent));
    }




}
