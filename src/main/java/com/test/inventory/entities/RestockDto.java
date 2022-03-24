package com.test.inventory.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestockDto {
    private String code;
    private String name;
    private Integer stock;
}
