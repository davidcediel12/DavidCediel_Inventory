package com.test.inventory.utils.mappers;

import com.test.inventory.dtos.ClientDto;
import com.test.inventory.entities.Client;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDto toDto(Client client);
    List<ClientDto> toListDto(List<Client> clients);

    Client toEntity(ClientDto clientDto);
}
