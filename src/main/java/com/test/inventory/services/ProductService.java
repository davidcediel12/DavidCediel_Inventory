package com.test.inventory.services;

import com.test.inventory.dtos.ProductBasicInformation;

import java.util.List;

public interface ProductService {
    List<ProductBasicInformation> obtainAllProducts();
    boolean updateStock(String productCode, Integer stock);
}
