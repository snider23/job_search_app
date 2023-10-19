package com.example.jobsearch.controller;

import com.example.jobsearch.domain.Position;
import com.example.jobsearch.dto.outgoing.PositionListItem;
import com.example.jobsearch.service.ClientService;
import com.example.jobsearch.service.PositionService;
import com.example.jobsearch.service.ReedJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class PositionControllerTest {

    private PositionController positionController;

    @Mock
    private PositionService positionService;

    @Mock
    private ClientService clientService;

    @Mock
    private ReedJob reedJob;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        positionController = new PositionController("reed-api-url", positionService, (WebClient.Builder) WebTestClient.bindToServer().baseUrl("http://localhost").build(), clientService, reedJob);
    }

    @Test
    public void testGetAllJobs2_ValidApiKey() {
        when(clientService.isApiKeyExists(anyString())).thenReturn(true);

        List<PositionListItem> jobListings = Arrays.asList(new PositionListItem());
        when(reedJob.getJobsFromExternalApi(any(Position.class), anyString())).thenReturn(Mono.just(jobListings));

        Mono<Object> result = positionController.getAllJobs2(new Position(), "valid-api-key");

        verify(clientService, times(1)).isApiKeyExists("valid-api-key");
    }



}
