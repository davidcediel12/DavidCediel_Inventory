package com.test.inventory.services.implementation;

import com.test.inventory.dtos.ProductBasicInformation;
import com.test.inventory.entities.Product;
import com.test.inventory.repositories.ProductRepository;
import com.test.inventory.services.ProductService;
import com.test.inventory.utils.mappers.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductBasicInformation> obtainAllProducts(){
        return productMapper.toListBasicInformation(productRepository.findAll());
    }

    @Override
    public boolean updateStock(String productCode, Integer stock){
        Product product = productRepository.getByCode(productCode);
        if(product == null) {
            log.info("Trying to update the stock of a product that doesn't exist");
            return false;
        }
        product.setStock(stock);
        productRepository.save(product);
        return true;

    }

}
