package com.test.inventory.controllers;

import com.test.inventory.InventoryApplication;
import com.test.inventory.dtos.ClientDto;
import com.test.inventory.entities.Client;
import com.test.inventory.repositories.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = InventoryApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientControllerIT {

    @LocalServerPort
    private Integer port;

    @MockBean
    ClientRepository clientRepository;

    @Test
    public void findClientByIdTestSuccess(){
        Mockito.when(clientRepository.findByIdentification(Mockito.anyString()))
                .thenReturn(Optional.of(mockClient()));
        String url = "http://localhost:" + port + "/clients/982922";

        TestRestTemplate restTemplate = new TestRestTemplate();

        RequestEntity<?> request = RequestEntity.get(url)
                .accept(MediaType.APPLICATION_JSON).build();

        ResponseEntity<ClientDto> response = restTemplate.exchange(
                request, ClientDto.class);

        Assertions.assertEquals(response.getBody().getName(), "MockName");
        Assertions.assertEquals(response.getBody().getLastname(), "MockLastName");
        Assertions.assertEquals(response.getBody().getIdentification(), "982922");


    }


    private Client mockClient(){
        return Client.builder()
                .identification("982922")
                .name("MockName")
                .lastname("MockLastName")
                .build();
    }
}
