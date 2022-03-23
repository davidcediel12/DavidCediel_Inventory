package com.test.inventory.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductBasicInformation {
    private String code;
    private String name;
}
