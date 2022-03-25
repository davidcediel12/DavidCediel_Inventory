package com.test.inventory.controllers;


import com.test.inventory.dtos.ProductBasicInformation;
import com.test.inventory.dtos.exception.ApplicationException;
import com.test.inventory.dtos.exception.ErrorDetails;
import com.test.inventory.entities.Client;
import com.test.inventory.services.ProductService;
import com.test.inventory.services.implementation.ProductServiceImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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
    public ResponseEntity<Resource> throwException() throws IOException {
        Client client1 = Client.builder()
                .identification("1280191")
                .name("Pedro")
                .lastname("Perez")
                .build();


        Client client2 = Client.builder()
                .identification("87387286")
                .name("David")
                .lastname("Cediel")
                .build();

        List<Client> csvBody = new ArrayList<>();
        csvBody.add(client1);
        csvBody.add(client2);

        ByteArrayInputStream in;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out),
                CSVFormat.DEFAULT);

        csvPrinter.printRecord(Arrays.asList("Identification", "Name", "LastName"));
        for(Client c : csvBody){
            List<String> data = Arrays.asList(
                    c.getIdentification(),
                    c.getName(),
                    c.getLastname()
            );
            csvPrinter.printRecord(data);

        }


        csvPrinter.flush();

        in = new ByteArrayInputStream(out.toByteArray());
        InputStreamResource fileInputStream = new InputStreamResource(in);
        String csvFileName = "people.csv";

        // setting HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFileName);
        // defining the custom Content-Type
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");

        return new ResponseEntity<>(
                fileInputStream,
                headers,
                HttpStatus.OK
        );
    }



}
