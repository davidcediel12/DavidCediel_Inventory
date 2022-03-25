package com.test.inventory.controllers;

import com.test.inventory.dtos.ClientDto;
import com.test.inventory.dtos.exception.ApplicationException;
import com.test.inventory.dtos.exception.ErrorDetails;
import com.test.inventory.services.ClientService;
import com.test.inventory.utils.controllererrors.ControllerErrors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients")
    public ResponseEntity<List<ClientDto>> obtainAllClients(){
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/clients/{identification}")
    public ResponseEntity<ClientDto> obtainClientByIdentification(
            @PathVariable String identification){
        return ResponseEntity.ok(clientService.findByIdentification(identification));
    }

    @PutMapping("/clients/{identification}")
    public ResponseEntity<ClientDto> updateClient(
            @PathVariable String identification,
            @RequestBody @Valid ClientDto clientDto){
        if(!clientService.updateClient(identification, clientDto))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/clients")
    public ResponseEntity createClient(@Valid @RequestBody ClientDto clientDto,
                                       BindingResult result) {
        if(result.hasErrors()){
            throw  ApplicationException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .errorDetails(ControllerErrors.resultErrorsToErrorDetails(result))
                    .build();
        }
        if(!clientService.createClient(clientDto))
            return ResponseEntity.badRequest().build();

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @DeleteMapping("/clients/{identification}")
    public ResponseEntity<Boolean> deleteByIdentification(
            @PathVariable String identification){
        return ResponseEntity.ok(clientService.deleteClientByIdentification(identification));
    }



}
