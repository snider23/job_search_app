package com.example.intuitech_hazi.service;

import com.example.intuitech_hazi.domain.Client;
import com.example.intuitech_hazi.dto.ClientListItem;
import com.example.intuitech_hazi.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public boolean isApiKeyExists(UUID apiKey){
        return !clientRepository.findByApiKey(apiKey).isEmpty();
    }
}
