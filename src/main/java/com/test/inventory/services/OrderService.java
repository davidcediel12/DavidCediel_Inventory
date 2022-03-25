package com.test.inventory.services;

import com.test.inventory.dtos.BigOrderDto;
import com.test.inventory.dtos.OrdersByDateAndStore;
import com.test.inventory.dtos.SoldProducts;

import java.util.List;

public interface OrderService {

    boolean createOrderResume(BigOrderDto bigOrderDto);
    List<OrdersByDateAndStore> obtainNumberOfOrdersByDateAndStore();
    List<SoldProducts> obtainNumberOfSoldProductsByStore();
}
