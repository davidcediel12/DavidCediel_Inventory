package com.test.inventory.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClientOrderDetails {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime transactionTimestamp;
    private String storeCode;
    private String productCode;
    private Integer numberOfItems;
    private Double totalPrice;

}
