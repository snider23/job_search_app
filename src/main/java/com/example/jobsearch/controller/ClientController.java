package com.example.jobsearch.controller;

import com.example.jobsearch.domain.Client;
import com.example.jobsearch.dto.incoming.ClientSaveCommand;
import com.example.jobsearch.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;


    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
    @PostMapping
    public ResponseEntity<String> registerNewClient(@RequestBody ClientSaveCommand newClient) {
        if (clientService.isEmailInUse(newClient.getEmail())) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already in use");

        }

        Client result = clientService.registerClient(newClient);
        HttpHeaders header= new HttpHeaders();
        header.add("apiKey",result.getApiKey());
        return new  ResponseEntity<>(header,HttpStatus.CREATED);
    }


}
