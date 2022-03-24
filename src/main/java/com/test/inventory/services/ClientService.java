package com.test.inventory.services;

import com.test.inventory.dtos.ClientDto;

import java.util.List;

public interface ClientService {
    List<ClientDto> getAllClients();
    ClientDto findByIdentification(String identification);
    boolean updateClient(String identification, ClientDto clientDto);
    boolean deleteClientByIdentification(String identification);
    boolean createClient(ClientDto client);
}
