package com.test.inventory.services.implementation;

import com.test.inventory.entities.OrderResume;
import com.test.inventory.repositories.OrderResumeRepository;
import com.test.inventory.services.OrderResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderResumeServiceImplementation implements OrderResumeService {

    private final OrderResumeRepository orderResumeRepository;

    @Autowired
    public OrderResumeServiceImplementation(OrderResumeRepository orderResumeRepository) {
        this.orderResumeRepository = orderResumeRepository;
    }

    @Override
    public List<OrderResume> obtainAllOrderResume() {
        return orderResumeRepository.findAll();
    }
}
