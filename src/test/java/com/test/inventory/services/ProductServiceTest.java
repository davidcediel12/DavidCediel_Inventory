package com.test.inventory.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.inventory.dtos.ProductBasicInformation;
import com.test.inventory.entities.Product;
import com.test.inventory.repositories.ProductRepository;
import com.test.inventory.utils.mappers.ProductMapper;
import com.test.inventory.utils.mappers.ProductMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ProductService.class, ProductMapper.class, ProductMapperImpl.class})
public class ProductServiceTest {
    @MockBean
    private  ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    private ProductService productService;

    @BeforeEach
    public  void mockProducts(){
        Mockito.when(productRepository.findAll()).thenReturn(Arrays.asList(
                Product.builder()
                        .id(12)
                        .code("prod-code")
                        .name("prod-name")
                        .price(Double.valueOf(12.2))
                        .stock(18)
                        .build()
        ));
    }
    @Test
    public void obtainAllProductsSuccess(){
        List<ProductBasicInformation> productsDto = productService.obtainAllProducts();
        List<ProductBasicInformation> expectedProducts = Arrays.asList(
                ProductBasicInformation.builder()
                        .code("prod-code")
                        .name("prod-name")
                        .build());
        Assertions.assertArrayEquals(expectedProducts.toArray(), productsDto.toArray());
    }
}
