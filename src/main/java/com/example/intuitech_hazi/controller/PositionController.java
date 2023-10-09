package com.example.intuitech_hazi.controller;

import com.example.intuitech_hazi.domain.Position;
import com.example.intuitech_hazi.dto.outgoing.PositionListItem;
import com.example.intuitech_hazi.service.ClientService;
import com.example.intuitech_hazi.service.PositionService;
import com.example.intuitech_hazi.service.ReedJob;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;



import java.util.ArrayList;
import java.util.Collections;
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

    @GetMapping("/jobs")
    public Mono<Object> getJobs(@RequestBody Position position, @RequestHeader("apiKey") String apiKey) {
        if (!clientService.isApiKeyExists(apiKey)) {
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid API key"));
        }
        List<PositionListItem> allPositionFromBothDB = new ArrayList<>();


        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/1.0/search")
                        .queryParam("keywords", position.getTitle())
                        .queryParam("locationName", position.getLocation())
                        .queryParam("resultsToTake", "5")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .headers(headers -> headers.setBasicAuth(externalApiKey, ""))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(json -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        JsonNode rootNode = mapper.readTree(json);
                        JsonNode resultsNode = rootNode.get("results");
                        List<PositionListItem> jobs = new ArrayList<>();
                        for (JsonNode resultNode : resultsNode) {
                            PositionListItem job = new PositionListItem();
                            job.setJobTitle(resultNode.get("jobTitle").asText());
                            job.setLocationName(resultNode.get("locationName").asText());
                            job.setJobUrl(resultNode.get("jobUrl").asText());
                            jobs.add(job);
                        }
                        allPositionFromBothDB.addAll(jobs);
                        allPositionFromBothDB.addAll(positionService.getComplexPositions(position, apiKey));
                        return Mono.just(allPositionFromBothDB);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return Mono.just(Collections.emptyList());
                    }

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
