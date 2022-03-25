package com.test.inventory.services;


import com.test.inventory.dtos.BigOrderDto;
import com.test.inventory.dtos.ProductOrder;
import com.test.inventory.dtos.StoreOrder;
import com.test.inventory.dtos.exception.ApplicationException;
import com.test.inventory.entities.Client;
import com.test.inventory.entities.Product;
import com.test.inventory.entities.Store;
import com.test.inventory.repositories.*;
import com.test.inventory.services.implementation.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {OrderServiceImpl.class})
public class OrderServiceTest {
    @MockBean
    private  OrderRepository orderRepository;
    @MockBean
    private  OrderResumeRepository orderResumeRepository;
    @MockBean
    private  ClientRepository clientRepository;
    @MockBean
    private  StoreRepository storeRepository;
    @MockBean
    private  ProductRepository productRepository;
    @Autowired
    private OrderService orderService;

    @Test
    public void createOrderResumeClientNotExists(){
        when(clientRepository.existsByIdentification(Mockito.anyString())).thenReturn(false);

        BigOrderDto mockBigOrder = mockBigOrderDtoMain(null);
        try{
            orderService.createOrderResume(mockBigOrder);
            fail();
        }catch(ApplicationException exception){
            assertEquals("Client not found to create the order", exception.getErrorDetails().getDescription());
        }
    }

    @Test
    public void createOrderResumeStoreNotExists(){
        when(clientRepository.existsByIdentification(Mockito.anyString())).thenReturn(true);
        when(clientRepository.findByIdentification(Mockito.anyString()))
                .thenReturn(Optional.of(mockClient()));
        when(storeRepository.findByCode(Mockito.anyString())).thenReturn(Optional.empty());

        BigOrderDto mockBigOrder = mockBigOrderDtoMain(null);
        try{
            orderService.createOrderResume(mockBigOrder);
            fail();
        }catch(ApplicationException exception){
            assertEquals("Store not found to create the order", exception.getErrorDetails().getDescription());
        }
    }

    @Test
    public void createOrderResumeProductNotFound(){
        when(clientRepository.existsByIdentification(Mockito.anyString())).thenReturn(true);
        when(clientRepository.findByIdentification(Mockito.anyString()))
                .thenReturn(Optional.of(mockClient()));
        when(storeRepository.findByCode(Mockito.anyString()))
                .thenReturn(Optional.of(mockStore()));
        when(productRepository.findByCode(Mockito.anyString())).thenReturn(Optional.empty());


        BigOrderDto mockBigOrder = mockBigOrderDtoMain(null);
        try{
            orderService.createOrderResume(mockBigOrder);
            fail();
        }catch(ApplicationException exception){
            assertEquals("Product not found to create the order", exception.getErrorDetails().getDescription());
        }
    }

    @Test
    public void createOrderResumeStoreNotSellProduct(){
        when(clientRepository.existsByIdentification(Mockito.anyString())).thenReturn(true);
        when(clientRepository.findByIdentification(Mockito.anyString()))
                .thenReturn(Optional.of(mockClient()));

        when(storeRepository.findByCode(Mockito.anyString()))
                .thenReturn(Optional.of(mockStore()));
        when(productRepository.findByCode(Mockito.anyString()))
                .thenReturn(Optional.of(mockProduct()));

        BigOrderDto mockBigOrder = mockBigOrderDtoMain(null);
        try{
            orderService.createOrderResume(mockBigOrder);
            fail();
        }catch(ApplicationException exception){
            assertEquals("The store don't sell the product", exception.getErrorDetails().getMessage());
        }
    }

    @Test
    public void createOrderResumeStoreNoStock(){
        when(clientRepository.existsByIdentification(Mockito.anyString())).thenReturn(true);
        when(clientRepository.findByIdentification(Mockito.anyString()))
                .thenReturn(Optional.of(mockClient()));
        Product product = mockProduct();
        Store mockStore = mockStore();
        mockStore.addProduct(product);
        when(storeRepository.findByCode(Mockito.anyString()))
                .thenReturn(Optional.of(mockStore));
        when(productRepository.findByCode(Mockito.anyString()))
                .thenReturn(Optional.of(product));

        BigOrderDto mockBigOrder = mockBigOrderDtoMain(null);
        try{
            orderService.createOrderResume(mockBigOrder);
            fail();
        }catch(ApplicationException exception){
            assertTrue(exception.getErrorDetails().getMessage().contains("Stock not enough for product"));
        }
    }

    @Test
    public void createOrderResumeStoreTenUnitsDeficit(){
        when(clientRepository.existsByIdentification(Mockito.anyString())).thenReturn(true);
        when(clientRepository.findByIdentification(Mockito.anyString()))
                .thenReturn(Optional.of(mockClient()));
        Product product = mockProduct();
        Store mockStore = mockStore();
        mockStore.addProduct(product);
        when(storeRepository.findByCode(Mockito.anyString()))
                .thenReturn(Optional.of(mockStore));
        when(productRepository.findByCode(Mockito.anyString()))
                .thenReturn(Optional.of(product));

        BigOrderDto mockBigOrder = mockBigOrderDtoMain(20);

        assertTrue(orderService.createOrderResume(mockBigOrder));

    }

    @Test
    public void createOrderResumeStoreFiveUnitsDeficit(){
        when(clientRepository.existsByIdentification(Mockito.anyString())).thenReturn(true);
        when(clientRepository.findByIdentification(Mockito.anyString()))
                .thenReturn(Optional.of(mockClient()));
        Product product = mockProduct();
        Store mockStore = mockStore();
        mockStore.addProduct(product);
        when(storeRepository.findByCode(Mockito.anyString()))
                .thenReturn(Optional.of(mockStore));
        when(productRepository.findByCode(Mockito.anyString()))
                .thenReturn(Optional.of(product));

        BigOrderDto mockBigOrder = mockBigOrderDtoMain(15);

        assertTrue(orderService.createOrderResume(mockBigOrder));

    }

    @Test
    public void createOrderResumeStoreFiveNoDeficit(){
        when(clientRepository.existsByIdentification(Mockito.anyString())).thenReturn(true);
        when(clientRepository.findByIdentification(Mockito.anyString()))
                .thenReturn(Optional.of(mockClient()));
        Product product = mockProduct();
        Store mockStore = mockStore();
        mockStore.addProduct(product);
        when(storeRepository.findByCode(Mockito.anyString()))
                .thenReturn(Optional.of(mockStore));
        when(productRepository.findByCode(Mockito.anyString()))
                .thenReturn(Optional.of(product));

        BigOrderDto mockBigOrder = mockBigOrderDtoMain(5);

        assertTrue(orderService.createOrderResume(mockBigOrder));

    }



    private BigOrderDto mockBigOrderDto(Integer units){
        return BigOrderDto.builder()
                .clientIdentification("289219")
                .storeOrders(Arrays.asList(
                        StoreOrder.builder()
                                .storeCode("store-1")
                                .productOrders(Arrays.asList(ProductOrder.builder()
                                        .numberOfItems(units)
                                        .productCode("prod-1")
                                        .build()))
                                .build()
                        ))
                .build();
    }

    private BigOrderDto mockBigOrderDtoMain(Integer units){
        units = units != null ? units : 100;
        return mockBigOrderDto(units);
    }

    private Client mockClient(){
        return Client.builder()
                .identification("982922")
                .name("MockName")
                .lastname("MockLastName")
                .build();
    }

    private Product mockProduct(){
        return Product.builder()
                .id(1)
                .code("amz")
                .price(12.5d)
                .stock(10)
                .build();
    }

    private Store mockStore(){
        return Store.builder()
                .id(1)
                .code("cod1")
                .name("storeName")
                .products(new HashSet<>())
                .build();
    }
}
