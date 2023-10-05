package com.example.intuitech_hazi.controller;

import com.example.intuitech_hazi.domain.Client;
import com.example.intuitech_hazi.repository.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostMapping
    public ResponseEntity<?> registerNewClient(@RequestBody Client newClient) {
        if (clientRepository.existsByEmail(newClient.getEmail())) {
          //  return ResponseEntity.badRequest().body("Email already in use");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already in use");

        }
        newClient.setApiKey(UUID.randomUUID());
        Client createdClient= clientRepository.save(newClient);
       // return ResponseEntity.ok(newClient.getApiKey());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient.getApiKey());
    }
}
