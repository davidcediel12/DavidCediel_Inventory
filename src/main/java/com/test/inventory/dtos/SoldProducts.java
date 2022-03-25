package com.test.inventory.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SoldProducts {
    private String storeCode;
    private String productCode;
    private Integer numberOfSoldProducts;
}
