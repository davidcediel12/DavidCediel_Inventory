package com.test.inventory.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.inventory.entities.Product;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductsDto {
    @JsonProperty("prods")
    private List<Product> products;
}
