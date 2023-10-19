package com.example.jobsearch.service;

import com.example.jobsearch.domain.Client;

import com.example.jobsearch.dto.incoming.ClientSaveCommand;
import com.example.jobsearch.repository.ClientRepository;
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
