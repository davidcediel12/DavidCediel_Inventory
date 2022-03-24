package com.test.inventory.dtos;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BigOrderDto {
    private List<StoreOrder> storeOrders;
}
