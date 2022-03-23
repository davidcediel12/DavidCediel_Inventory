package com.test.inventory.services;

import com.test.inventory.dtos.ClientDto;

import java.util.List;

public interface ClientService {
    List<ClientDto> getAllClients();
    ClientDto findByIdentification();
    ClientDto updateClient(String identification);
    boolean deleteClientByIdentification(String identification);
    ClientDto createClient(ClientDto client);
}
