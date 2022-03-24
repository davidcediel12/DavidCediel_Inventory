package com.test.inventory.controllers;


import com.test.inventory.entities.OrderResume;
import com.test.inventory.services.OrderResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderResumeController {

    private final OrderResumeService orderResumeService;

    @Autowired
    public OrderResumeController(OrderResumeService orderResumeService) {
        this.orderResumeService = orderResumeService;
    }

    @GetMapping("/orderResumes")
    public ResponseEntity<List<OrderResume>> obtainAllOrderResume(){
        return ResponseEntity.ok(orderResumeService.obtainAllOrderResume());
    }
}
