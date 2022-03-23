package com.test.inventory.utils.mappers;


import com.test.inventory.dtos.ProductBasicInformation;
import com.test.inventory.entities.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductBasicInformation toBasicInformation(Product product);
    List<ProductBasicInformation> toListBasicInformation(List<Product> products);

}
