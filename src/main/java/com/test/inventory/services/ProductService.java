package com.test.inventory.services;

import com.test.inventory.dtos.ProductBasicInformation;
import com.test.inventory.repositories.ProductRepository;
import com.test.inventory.utils.mappers.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Autowired
    public ProductService(ProductRepository productRepository,
                          ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductBasicInformation> obtainAllProducts(){
        return productMapper.toListBasicInformation(productRepository.findAll());
    }

}
