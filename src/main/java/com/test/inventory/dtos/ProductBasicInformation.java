package com.test.inventory.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ProductBasicInformation {
    private String code;
    private String name;
    private Integer stock;
}
