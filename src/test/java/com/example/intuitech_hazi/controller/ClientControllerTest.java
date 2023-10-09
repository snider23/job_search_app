package com.example.intuitech_hazi.controller;

import com.example.intuitech_hazi.domain.Client;
import com.example.intuitech_hazi.dto.incoming.ClientSaveCommand;
import com.example.intuitech_hazi.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@WebMvcTest(ClientController.class)
@ExtendWith(SpringExtension.class)
public class ClientControllerTest {

    private ClientController clientController;

    @Mock
    private ClientService clientService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        clientController = new ClientController(clientService);
    }

    @Test
    public void testRegisterNewClient() {
        ClientSaveCommand clientSaveCommand = new ClientSaveCommand();
        clientSaveCommand.setEmail("test@example.com");

        when(clientService.isEmailInUse(clientSaveCommand.getEmail())).thenReturn(false);

        Client resultClient = new Client();
        resultClient.setApiKey("some-api-key");
        when(clientService.registerClient(clientSaveCommand)).thenReturn(resultClient);

        ResponseEntity<String> responseEntity = clientController.registerNewClient(clientSaveCommand);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("some-api-key", responseEntity.getHeaders().get("apiKey").get(0));
    }

    @Test
    public void testRegisterNewClient_EmailInUse() {
        ClientSaveCommand clientSaveCommand = new ClientSaveCommand();
        clientSaveCommand.setEmail("test@example.com");

        when(clientService.isEmailInUse(clientSaveCommand.getEmail())).thenReturn(true);

        ResponseEntity<String> responseEntity = clientController.registerNewClient(clientSaveCommand);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Email is already in use", responseEntity.getBody());
    }




}
