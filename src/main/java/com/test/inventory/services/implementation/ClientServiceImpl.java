package com.test.inventory.services.implementation;

import com.test.inventory.dtos.ClientDto;
import com.test.inventory.services.ClientService;

import java.util.List;

public class ClientServiceImpl implements ClientService {
    @Override
    public List<ClientDto> getAllClients() {
        return null;
    }

    @Override
    public ClientDto findByIdentification() {
        return null;
    }

    @Override
    public ClientDto updateClient(String identification) {
        return null;
    }

    @Override
    public boolean deleteClientByIdentification(String identification) {
        return false;
    }

    @Override
    public ClientDto createClient(ClientDto client) {
        return null;
    }
}
