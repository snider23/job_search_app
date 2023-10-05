package com.example.intuitech_hazi.controller;

import com.example.intuitech_hazi.domain.Client;
import com.example.intuitech_hazi.repository.ClientRepository;
import com.example.intuitech_hazi.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

//    public static final UUID MY_CONSTANT = UUID.randomUUID();

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
    @PostMapping
    public ResponseEntity<String> registerNewClient(@RequestBody Client newClient) {
        if (clientService.isEmailInUse(newClient.getEmail())) {
          //  return ResponseEntity.badRequest().body("Email already in use");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already in use");

        }

        Client result = clientService.registerClient(newClient);
        return new  ResponseEntity<>(result.getApiKey().toString(),HttpStatus.CREATED);
        //return ResponseEntity.status(HttpStatus.CREATED).body(result.getApiKey().toString());
    }
}