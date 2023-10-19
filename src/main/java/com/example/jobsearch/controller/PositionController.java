package com.example.jobsearch.controller;

import com.example.jobsearch.domain.Position;
import com.example.jobsearch.dto.outgoing.PositionListItem;
import com.example.jobsearch.service.ClientService;
import com.example.jobsearch.service.PositionService;
import com.example.jobsearch.service.ReedJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/positions")
@Validated
public class PositionController {

    private PositionService positionService;
    private ClientService clientService;

    private WebClient webClient;

    private ReedJob reedJob;

    @Value("${reed.api.key}")
    private String externalApiKey;

    public PositionController(
            @Value("${reed.url}") String websiteUrl,
            PositionService positionService,
            WebClient.Builder webClientBuilder,
            ClientService clientService,
            ReedJob reedJob) {
        this.positionService = positionService;
        this.clientService = clientService;
        this.webClient = webClientBuilder
                .baseUrl(websiteUrl)
                .build();
        this.reedJob = reedJob;
        this.externalApiKey = reedJob.getExternalApiKey();
    }


    @GetMapping("/search")
    public Mono<Object> getAllJobs2(@RequestBody Position position, @RequestHeader("apiKey") String apiKey) {
        if (!clientService.isApiKeyExists(apiKey)) {
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid API key"));
        }

        return reedJob.getJobsFromExternalApi(position, apiKey)
                .flatMap(jobs -> {
                    List<PositionListItem> allPositionFromBothDB = new ArrayList<>();
                    allPositionFromBothDB.addAll(jobs);
                    allPositionFromBothDB.addAll(positionService.getComplexPositions(position, apiKey));
                    return Mono.just(allPositionFromBothDB);
                });
    }

    @PostMapping
    public ResponseEntity<String> createNewJob(@RequestBody Position newPosition,
                                               @RequestHeader("apiKey") String apiKey) {
        if (!clientService.isApiKeyExists(apiKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid API key");
        } else {

            positionService.createNewPosition(newPosition);

            return ResponseEntity.status(HttpStatus.CREATED).body(newPosition.getJobUrl());
        }
    }


}
