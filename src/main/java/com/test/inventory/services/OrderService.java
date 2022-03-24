package com.test.inventory.services;

import com.test.inventory.dtos.BigOrderDto;

public interface OrderService {

    boolean createOrderResume(BigOrderDto bigOrderDto);
}
