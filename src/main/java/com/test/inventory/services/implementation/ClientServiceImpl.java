package com.test.inventory.services.implementation;

import com.test.inventory.dtos.ClientDto;
import com.test.inventory.entities.Client;
import com.test.inventory.repositories.ClientRepository;
import com.test.inventory.services.ClientService;
import com.test.inventory.utils.mappers.ClientMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDto> getAllClients() {
        return clientMapper.toListDto(clientRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDto findByIdentification(String identification) {
        return clientMapper.toDto(clientRepository.findByIdentification(
                identification).orElse(null));

    }

    @Override
    @Transactional
    public boolean updateClient(String identification, ClientDto clientDto) {
        if(!clientRepository.existsByIdentification(identification))
            return false;

        Client client = clientRepository.findByIdentification(identification).orElse(null);
        Client updatedClient = clientMapper.toEntity(clientDto);
        updatedClient.setId(client.getId());

        clientRepository.save(updatedClient);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteClientByIdentification(String identification) {
        clientRepository.deleteByIdentification(identification);
        return true;
    }

    @Override
    @Transactional
    public boolean createClient(ClientDto client) {
        if(clientRepository.existsByIdentification(client.getIdentification()))
            return false;
        log.info("-------------SAVING CLIENT");
        clientRepository.save(clientMapper.toEntity(client));
        return true;
    }
}
