package com.test.inventory.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOrder {
    private String productCode;
    private Integer nItems;
}
