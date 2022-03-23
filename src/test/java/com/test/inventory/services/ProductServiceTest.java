package com.test.inventory.services;

import com.test.inventory.dtos.ProductBasicInformation;
import com.test.inventory.entities.Product;
import com.test.inventory.repositories.ProductRepository;
import com.test.inventory.services.implementation.ProductServiceImpl;
import com.test.inventory.utils.mappers.ProductMapper;
import com.test.inventory.utils.mappers.ProductMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ProductServiceImpl.class, ProductMapper.class, ProductMapperImpl.class})
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


    @Test
    public void updateStockSuccess(){
        Mockito.when(productRepository.getByCode(Mockito.anyString())).thenReturn(
                mockProduct());
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(new Product());
        Assertions.assertTrue(productService.updateStock("someCode", 10));
    }

    @Test
    public void updateStockNonProduct(){
        Mockito.when(productRepository.getByCode(Mockito.anyString())).thenReturn(null);
        Assertions.assertFalse(productService.updateStock("someCode", 10));
    }

    private Product mockProduct(){
        return Product.builder()
                .id(1)
                .code("code")
                .name("Product")
                .stock(10)
                .price(12.4)
                .build();
    }

}
