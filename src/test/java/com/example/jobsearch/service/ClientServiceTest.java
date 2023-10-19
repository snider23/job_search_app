package com.example.jobsearch.service;

import com.example.jobsearch.domain.Client;
import com.example.jobsearch.dto.incoming.ClientSaveCommand;
import com.example.jobsearch.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientServiceTest {

    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        clientService = new ClientService(clientRepository);
    }

    @Test
    public void testIsEmailInUse_EmailExists() {
        when(clientRepository.existsByEmail("existing@example.com")).thenReturn(true);

        boolean result = clientService.isEmailInUse("existing@example.com");

        assertTrue(result);
    }

    @Test
    public void testIsEmailInUse_EmailNotExists() {
        when(clientRepository.existsByEmail("new@example.com")).thenReturn(false);

        boolean result = clientService.isEmailInUse("new@example.com");

        assertFalse(result);
    }

    @Test
    public void testRegisterClient() {
        ClientSaveCommand newClient = new ClientSaveCommand();
        newClient.setEmail("new@example.com");
        newClient.setName("John Doe");

        Client savedClient = new Client(newClient);
        savedClient.setApiKey("unique-api-key");

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        Client result = clientService.registerClient(newClient);

        assertEquals("unique-api-key", result.getApiKey());
    }

    @Test
    public void testIsApiKeyExists_ApiKeyExists() {
        when(clientRepository.findByApiKey("existing-api-key")).thenReturn(Optional.of(new Client()));

        boolean result = clientService.isApiKeyExists("existing-api-key");

        assertTrue(result);
    }

    @Test
    public void testIsApiKeyExists_ApiKeyNotExists() {
        when(clientRepository.findByApiKey("non-existing-api-key")).thenReturn(Optional.empty());

        boolean result = clientService.isApiKeyExists("non-existing-api-key");

        assertFalse(result);
    }


}
