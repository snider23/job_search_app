package com.example.intuitech_hazi.service;

import com.example.intuitech_hazi.domain.Client;

import com.example.intuitech_hazi.dto.incoming.ClientSaveCommand;
import com.example.intuitech_hazi.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public boolean isEmailInUse(String email) {
        return clientRepository.existsByEmail(email);
    }

    public Client registerClient(ClientSaveCommand newClient) {
        String apiKey= UUID.randomUUID().toString();
        Client client= new Client(newClient);
        client.setApiKey(apiKey);
        return clientRepository.save(client);
    }
    
    public boolean isApiKeyExists(String apiKey){
        return clientRepository.findByApiKey(apiKey).isPresent();
    }
}
