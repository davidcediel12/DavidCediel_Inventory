package com.test.inventory.services;

import com.test.inventory.dtos.BigOrderDto;
import com.test.inventory.dtos.ClientOrderDetails;
import com.test.inventory.dtos.OrdersByDateAndStore;
import com.test.inventory.dtos.SoldProducts;
import org.springframework.core.io.InputStreamResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    boolean createOrderResume(BigOrderDto bigOrderDto);
    List<OrdersByDateAndStore> obtainNumberOfOrdersByDateAndStore();
    List<SoldProducts> obtainNumberOfSoldProductsByStore();

    InputStreamResource obtainOrdersClientBetweenDates(
            LocalDate startDate, LocalDate endDate, String clientIdentification);
}
