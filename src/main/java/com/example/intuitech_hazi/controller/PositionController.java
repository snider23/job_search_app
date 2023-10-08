package com.example.intuitech_hazi.controller;

import com.example.intuitech_hazi.domain.Position;
import com.example.intuitech_hazi.dto.outgoing.PositionListItem;
import com.example.intuitech_hazi.service.ClientService;
import com.example.intuitech_hazi.service.PositionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/positions")
@Validated
public class PositionController {

    private final String URL = "http://localhost:8080/positions/";

    private PositionService positionService;
    private ClientService clientService;

    private WebClient webClient;

    @Value("${reed.api.key}")
    private String externalApiKey;

    @Value("${reed.url}")
    private String websiteUrl;

//    public PositionController(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl("https://www.reed.co.uk").build();
//    }

    @Autowired
    public PositionController(PositionService positionService, ClientService clientService, WebClient.Builder webClientBuilder) {
        this.positionService = positionService;
        this.clientService = clientService;
        this.webClient = webClientBuilder.baseUrl("https://www.reed.co.uk")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .responseTimeout(Duration.ofMinutes(2))))
                .build();
    }


    @GetMapping("/jobs")
    public Mono<Object> getJobs(@RequestBody Position position, @RequestHeader("apiKey") String apiKey) {
        if (!clientService.isApiKeyExists(apiKey)){
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid API key"));
        }
        List<PositionListItem> allPositionFromBothDB = new ArrayList<>();


        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/1.0/search")
                        .queryParam("keywords",position.getTitle())
                        .queryParam("locationName",position.getLocation())
                        .queryParam("resultsToTake","5")
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
                        allPositionFromBothDB.addAll(getComplexPositions(position, apiKey));
                        return Mono.just(allPositionFromBothDB);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return Mono.just(Collections.emptyList());
                    }

                });
    }

    //TODO Jackson convertalas utan nezni


    @PostMapping    //TODO nem hozza letre postmannel megnezni, headerken add at az apikeyt
    public ResponseEntity<String> createNewJob(@RequestBody Position newPosition,
                                               @RequestHeader("apiKey") String apiKey) {
        if (!clientService.isApiKeyExists(apiKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Hibás API kulcs");
        } else {

            positionService.createNewPosition(newPosition);
//            newPosition.setTitle(newPosition.getTitle());
//            newPosition.setLocation(newPosition.getLocation());
//            Long positionId = newPosition.getId();
//            String positionUrl = URL + positionId;
//            positionService.getPositionById(positionId).setJobUrl(positionUrl);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPosition.getJobUrl());
        }
    }

    @GetMapping
    public List<Position> getPositions() {
        return positionService.getPositions();

    }


    @GetMapping("/{id}")
    public Position getPositions(@PathVariable Long id) {
        return positionService.getPositionById(id);
    }

    @GetMapping("/getjobs")
    public List<PositionListItem> getComplexPositions(@RequestBody Position position, @RequestHeader("apiKey") String apiKey) {
        if (!clientService.isApiKeyExists(apiKey)) {
            throw new IllegalArgumentException("api key not found");
        }

        return positionService.getPositionsByTitleOrLocation(position).stream().map(PositionListItem::new).collect(Collectors.toList());
    }

}
