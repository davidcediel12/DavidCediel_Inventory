package com.test.inventory.dtos;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Builder
public class BigOrderDto {
    @Valid
    @Size(min = 1)
    @NotNull
    private List<StoreOrder> storeOrders;

    @NotEmpty
    private String clientIdentification;
}
