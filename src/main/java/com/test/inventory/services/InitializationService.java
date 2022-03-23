package com.test.inventory.services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.inventory.dtos.ProductsDto;
import com.test.inventory.dtos.exception.ApplicationException;
import com.test.inventory.dtos.exception.ErrorDetails;
import com.test.inventory.entities.Product;
import com.test.inventory.entities.Store;
import com.test.inventory.repositories.ProductRepository;
import com.test.inventory.repositories.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Service
@Slf4j
public class InitializationService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient okHttpClient = new OkHttpClient();


    @Autowired
    public InitializationService(ProductRepository productRepository,
                                 StoreRepository storeRepository) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    private void sayHi() throws ApplicationException {
        List<Product> products = new ArrayList<>();
        try {
            products = this.obtainProducts();
        }catch (IOException e){
            log.error("Error while fetching the products from the remote api ".concat(e.getMessage()));
            throw ApplicationException.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorDetails(ErrorDetails.builder()
                            .message("Error while obtaining the first products from remote api")
                            .description(e.getMessage())
                            .build())
                    .build();
        }
        this.createStores(products);
    }

    private void createStores(List<Product> products){
        storeRepository.save(Store.builder()
                .code("amz")
                .name("Amazon")
                .products(new HashSet<>(products))
                .build());

        storeRepository.save(Store.builder()
                .code("ebay")
                .name("Ebay")
                .products(new HashSet<>(products.subList(1, products.size())))
                .build());

        storeRepository.save(Store.builder()
                .code("alb")
                .name("Alibaba")
                .products(new HashSet<>(products.subList(2, products.size())))
                .build());

        storeRepository.save(Store.builder()
                .code("mel")
                .name("Mercado Libre")
                .products(new HashSet<>(products.subList(3, products.size())))
                .build());

    }

    private List<Product> obtainProducts() throws IOException {
        Request request = new Request.Builder()
                .url("https://mocki.io/v1/ee9dd9b1-6e6c-4c93-a896-9434db28a3c1")
                .build();

        Call call = okHttpClient.newCall(request);
        Response response = call.execute();

        ProductsDto productsDto = objectMapper.readValue(response.body().string(), ProductsDto.class);
        for(Product product : productsDto.getProducts()){
            productRepository.save(product);
        }
        return productsDto.getProducts();
    }
}
