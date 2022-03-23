package com.test.inventory.controllers;


import com.test.inventory.dtos.ProductBasicInformation;
import com.test.inventory.services.ProductService;
import com.test.inventory.services.implementation.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriBuilder;

import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @MockBean
    ProductServiceImpl productService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void retrieveAllProductsSuccess() throws Exception {
        Mockito.when(productService.obtainAllProducts()).thenReturn(Arrays.asList(
                ProductBasicInformation.builder()
                        .code("mock-code")
                        .name("mock-name")
                        .build()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("http://localhost:8080/products")
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("[{code: mock-code, name:mock-name}]", true));
    }

    @Test
    public void updateProductStockBadStock() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("http://localhost:8080/products/prod-1/stock?stock=" + -20)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid stock"));
    }

    @Test
    public void updateProductStockNotFound() throws Exception {
        Mockito.when(productService.updateStock(Mockito.anyString(),
                Mockito.anyInt())).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("http://localhost:8080/products/prod-1/stock?stock=" + 20)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateProductStockSuccess() throws Exception {
        Mockito.when(productService.updateStock(Mockito.anyString(),
                Mockito.anyInt())).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("http://localhost:8080/products/prod-1/stock?stock=" + 20)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }
}
