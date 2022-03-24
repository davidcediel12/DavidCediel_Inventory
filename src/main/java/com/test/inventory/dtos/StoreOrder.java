package com.test.inventory.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StoreOrder {
    private String sotreCode;
    private List<ProductOrder> productOrders;
}
