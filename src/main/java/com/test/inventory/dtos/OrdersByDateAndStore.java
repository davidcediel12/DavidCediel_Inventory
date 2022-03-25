package com.test.inventory.dtos;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class OrdersByDateAndStore {
    private LocalDate orderDate;
    private String storeCode;
    private Integer numberOfOrders;
}
