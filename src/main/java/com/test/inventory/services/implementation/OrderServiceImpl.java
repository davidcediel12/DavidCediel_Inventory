package com.test.inventory.services.implementation;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.inventory.dtos.*;
import com.test.inventory.dtos.exception.ApplicationException;
import com.test.inventory.dtos.exception.ErrorDetails;
import com.test.inventory.entities.*;
import com.test.inventory.repositories.*;
import com.test.inventory.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderResumeRepository orderResumeRepository;
    private final ClientRepository clientRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private static final String NOT_FOUND = " not found";
    public OrderServiceImpl(
            OrderRepository orderRepository, OrderResumeRepository orderResumeRepository,
            ClientRepository clientRepository, StoreRepository storeRepository,
            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderResumeRepository = orderResumeRepository;
        this.clientRepository = clientRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    /**
     * Entry point to create the order, it validates the existence of the clients
     * and store, then, for each product bought in the store, call the method
     * {@link {#chooseOrderCreation() ChooseOrderCreation} to decide how to make
     * (or reject) the order of the product depending on the stock and the desired quantity
     * @param bigOrderDto contains all the information about the order
     * @return true if the transaction is made, false otherwise
     */
    @Override
    @Transactional
    public boolean createOrderResume(BigOrderDto bigOrderDto) {
        if(!clientRepository.existsByIdentification(bigOrderDto.getClientIdentification()))
            throw ApplicationException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .errorDetails(ErrorDetails.builder()
                            .message("Client " + bigOrderDto.getClientIdentification() + NOT_FOUND)
                            .description("Client not found to create the order")
                            .build())
                    .build();

        Client client = clientRepository.findByIdentification(bigOrderDto.getClientIdentification()).get();
        OrderResume orderResume = OrderResume.builder()
                .client(client)
                .build();
        orderResumeRepository.save(orderResume);
        for(StoreOrder storeOrder : bigOrderDto.getStoreOrders()){
            Store store = storeRepository.findByCode(storeOrder.getStoreCode()).orElse(null);
            if(store == null)
                throw ApplicationException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .errorDetails(ErrorDetails.builder()
                                .message("Store " + storeOrder.getStoreCode() + NOT_FOUND)
                                .description("Store not found to create the order")
                                .build())
                        .build();
            for(ProductOrder productOrder : storeOrder.getProductOrders()){
                chooseOrderCreation(store, productOrder, orderResume);
            }
        }

        return true;
    }

    /**
     * Depending on the stock of the product, it chooses one way to save the order
     * @param store store where the product was bought
     * @param productOrder contains information about the order, like number of items and product
     * @param orderResume is the parent of the product order, if the order is made,
     *                    then the product is added to orderResume
     */
    private void chooseOrderCreation(Store store, ProductOrder productOrder, OrderResume orderResume){
        Product product = productRepository.findByCode(productOrder.getProductCode()).orElse(null);
        if(product == null)
            throwProductNotFound(productOrder.getProductCode());

        if(!store.getProducts().contains(product))
            throwStoreDoesntSellProduct(store.getCode(), product.getCode());

        if(product.getStock() - productOrder.getNumberOfItems() < -10)
            throwNotStock(product.getCode(), productOrder.getNumberOfItems(), product.getStock());

        else if(product.getStock() - productOrder.getNumberOfItems() >= -10 &&
                product.getStock() - productOrder.getNumberOfItems() < -5){

            restockProduct(product, RestockUnits.TEN);
            makeOrder(product, productOrder, store, orderResume);
        }else if(product.getStock() - productOrder.getNumberOfItems() >= -5 &&
                product.getStock() - productOrder.getNumberOfItems() < 0){
            new Thread(() -> {
                restockProduct(product, RestockUnits.FIVE);
                makeOrder(product, productOrder, store, orderResume);
            }).start();
        }else{
            makeOrder(product, productOrder, store, orderResume);
        }
    }

    /**
     * Perform all the data transaction to store the order
     * @param product product to edit the stock
     * @param productOrder order to save
     * @param store store where the product was bought
     * @param orderResume orderResume to add the order
     */
    private void makeOrder(Product product, ProductOrder productOrder,
                           Store store, OrderResume orderResume){
        product.setStock(product.getStock() - productOrder.getNumberOfItems());
        Order order = Order.builder()
                .orderResume(orderResume)
                .store(store)
                .product(product)
                .items(productOrder.getNumberOfItems())
                .totalPrice(product.getPrice() * productOrder.getNumberOfItems())
                .build();
        orderRepository.save(order);
        productRepository.save(product);
        orderResume.addOrder(order);
        orderResumeRepository.save(orderResume);
    }

    /**
     * Calls a third party api to restock the product, depending on the needed stock
     * it calls a service
     * @param product product to restock
     * @param units number of units to restock
     */
    private void restockProduct(Product product, RestockUnits units)  {
        String url = "";
        if(units == RestockUnits.TEN)
            url = "https://mocki.io/v1/72c28d7b-3320-4b5e-8289-c901b35268ab";
        else if(units == RestockUnits.FIVE)
            url = "https://mocki.io/v1/ea458e70-6774-40c7-b646-6f3ec4a8e170";

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = okHttpClient.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw ApplicationException.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorDetails(ErrorDetails.builder()
                            .message("Error restocking the product " + product.getCode())
                            .description("Error calling the third party api")
                            .build())
                    .build();
        }

        RestockDto productRestockDto = null;
        try {
            productRestockDto = objectMapper.readValue(response.body().string(), RestockDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        product.setStock(product.getStock() + productRestockDto.getStock());
    }

    private void throwStoreDoesntSellProduct(String storeCode, String productCode){
        throw ApplicationException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errorDetails(ErrorDetails.builder()
                        .message("The store don't sell the product")
                        .description("The store " + storeCode + " don't sell the product: " + productCode)
                        .build())
                .build();
    }
    private void throwProductNotFound(String productCode){
        throw ApplicationException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errorDetails(ErrorDetails.builder()
                        .message("Product " + productCode + NOT_FOUND)
                        .description("Product not found to create the order")
                        .build())
                .build();
    }

    private void throwNotStock(String productCode, Integer items, Integer stock){
        throw ApplicationException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errorDetails(ErrorDetails.builder()
                        .message("Stock not enough for product " + productCode)
                        .description("The quantity available is " + stock +
                                " and the required is " + items)
                        .build())
                .build();
    }


    @Override
    public List<OrdersByDateAndStore> obtainNumberOfOrdersByDateAndStore(){
        List<Tuple> results =  orderRepository.obtainNumberOfOrdersByDateAndStore();
        List<OrdersByDateAndStore> orders = new ArrayList<>();

        for(Tuple result : results){
            orders.add(OrdersByDateAndStore.builder()
                    .orderDate(((Date)result.get("orderDate")).toLocalDate())
                    .numberOfOrders(((BigInteger)result.get("numberOfOrders")).intValue())
                    .storeCode(result.get("storeCode").toString())
                    .build());
        }

        return orders;
    }

    @Override
    public List<SoldProducts> obtainNumberOfSoldProductsByStore() {
        List<Tuple> results =  orderRepository.obtainNumberOfSoldProductsByStore();
        List<SoldProducts> soldProducts = new ArrayList<>();

        for(Tuple result : results){
            soldProducts.add(SoldProducts.builder()
                    .storeCode(result.get("storeCode").toString())
                    .productCode(result.get("productCode").toString())
                    .numberOfSoldProducts(((BigInteger) result.get("numberOfSoldProducts")).intValue())
                    .build());
        }
        return soldProducts;
    }

    @Override
    public InputStreamResource obtainOrdersClientBetweenDates(
            LocalDate startDate, LocalDate endDate, String clientIdentification)  {
        List<ClientOrderDetails> clientOrdersDetails = orderRepository.clientOrdersBetweenDates(
                LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX),
                clientIdentification);

        ByteArrayInputStream in;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        CSVPrinter csvPrinter = null;
        try {
            csvPrinter = new CSVPrinter(new PrintWriter(out),
                    CSVFormat.DEFAULT);
            csvPrinter.printRecord(Arrays.asList(
                    "date", "storecode", "productcode", "items", "totalprice"));
        } catch (IOException e) {
            errorCreatingCsv(e.getMessage());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for(ClientOrderDetails clientOrderDetails : clientOrdersDetails){
            List<String> data = Arrays.asList(
                    clientOrderDetails.getTransactionTimestamp().format(formatter),
                    clientOrderDetails.getStoreCode(),
                    clientOrderDetails.getProductCode(),
                    clientOrderDetails.getNumberOfItems().toString(),
                    clientOrderDetails.getTotalPrice().toString()
            );
            try {
                csvPrinter.printRecord(data);
            } catch (IOException e) {
                errorCreatingCsv(e.getMessage());
            }

        }
        try {
            csvPrinter.flush();
        } catch (IOException e) {
            errorCreatingCsv(e.getMessage());
        }

        in = new ByteArrayInputStream(out.toByteArray());
        InputStreamResource fileInputStream = new InputStreamResource(in);
        return fileInputStream;
    }


    private void errorCreatingCsv(String errorMessage){
        log.error("Error creating the CSV for client transactions");
        throw ApplicationException.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorDetails(ErrorDetails.builder()
                        .message("Error creating the CSV printer")
                        .description(errorMessage)
                        .build())
                .build();
    }

}
