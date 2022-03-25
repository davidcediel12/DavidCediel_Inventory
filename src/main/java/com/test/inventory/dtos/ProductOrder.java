package com.test.inventory.dtos;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class ProductOrder {
    @NotEmpty
    private String productCode;
    @Min(1)
    @NotNull
    private Integer numberOfItems;
}
