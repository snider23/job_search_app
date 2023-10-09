package com.example.intuitech_hazi.service;

import com.example.intuitech_hazi.domain.Position;
import com.example.intuitech_hazi.dto.outgoing.PositionListItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;


import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class ReedJob {

    private WebClient webClient;
    private ClientService clientService;
    private PositionService positionService;

    @Value("${reed.api.key}")
    private String externalApiKey;

    @Autowired
    public ReedJob(@Value("${reed.url}") String websiteUrl,
                   PositionService positionService,
                   ClientService clientService,
                   WebClient.Builder webClientBuilder) {
        this.positionService = positionService;
        this.clientService = clientService;
        this.webClient = webClientBuilder.baseUrl(websiteUrl)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .responseTimeout(Duration.ofMinutes(2))))
                .build();
    }


    public Mono<List<PositionListItem>> getJobsFromExternalApi(Position position, String apiKey) {
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
                .flatMap(this::processJsonResponse)
                .onErrorResume(JsonProcessingException.class, e -> {
                    e.printStackTrace();
                    return Mono.just(Collections.emptyList());
                });
    }

    private Mono<List<PositionListItem>> processJsonResponse(String json) {
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

            return Mono.just(jobs);
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }


    public WebClient getWebClient() {
        return webClient;
    }

    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public PositionService getPositionService() {
        return positionService;
    }

    public void setPositionService(PositionService positionService) {
        this.positionService = positionService;
    }

    public String getExternalApiKey() {
        return externalApiKey;
    }

    public void setExternalApiKey(String externalApiKey) {
        this.externalApiKey = externalApiKey;
    }


}
