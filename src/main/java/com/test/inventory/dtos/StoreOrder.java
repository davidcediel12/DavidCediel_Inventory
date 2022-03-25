package com.test.inventory.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class StoreOrder {
    @NotEmpty
    private String storeCode;
    @Valid
    @Size(min = 1)
    @NotNull
    private List<ProductOrder> productOrders;
}
