package com.test.inventory.services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.inventory.dtos.ProductsDto;
import com.test.inventory.entities.Product;
import com.test.inventory.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


import java.io.IOException;


@Service
@Slf4j
public class InitializationService {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient okHttpClient = new OkHttpClient();


    @Autowired
    public InitializationService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    private void sayHi() throws IOException {
        try {
            this.obtainProducts();
        }catch (IOException e){
            log.error("Error while fetching the products from the remote api ".concat(e.getMessage()));
            // TODO ADD EXCEPTION HANDLER
        }
    }

    private void obtainProducts() throws IOException {
        Request request = new Request.Builder()
                .url("https://mocki.io/v1/ee9dd9b1-6e6c-4c93-a896-9434db28a3c1")
                .build();

        Call call = okHttpClient.newCall(request);
        Response response = call.execute();

        ProductsDto productsDto = objectMapper.readValue(response.body().string(), ProductsDto.class);
        for(Product product : productsDto.getProducts()){
            productRepository.save(product);
        }
    }
}
