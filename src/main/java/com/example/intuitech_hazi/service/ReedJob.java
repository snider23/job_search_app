package com.example.intuitech_hazi.service;

import com.example.intuitech_hazi.domain.Position;
import com.example.intuitech_hazi.dto.outgoing.PositionListItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.net.URI;
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

    public ReedJob(WebClient.Builder builder, @Value("${reed.url}") String websiteUrl){
        this.webClient= builder
                .baseUrl(websiteUrl)
                .build();
    }

//    public List<PositionListItem> getJobsFromReed(@RequestBody Position position){
//
//        ResponseEntity<List<PositionListItem>> response = webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/api/1.0/search")
//                        .queryParam("keywords", position.getTitle())
//                        .queryParam("locationName", position.getLocation())
//                        .queryParam("resultsToTake", "5")
//                        .build())
//                        .accept(MediaType.APPLICATION_JSON)
//                        .headers(headers -> headers.setBasicAuth(externalApiKey, ""))
//                        .retrieve()
//                        .bodyToMono(String.class)
//                        .flatMap(json -> processJsonResponse(json, allPositionFromBothDB)););
//
//    }

    public List<PositionListItem> processJsonResponse(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
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

        return jobs;
    }


    public Mono<Object> getJobs(@RequestBody Position position, @RequestHeader("apiKey") String apiKey) {
        if (!clientService.isApiKeyExists(apiKey)) {
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid API key"));
        }
        List<PositionListItem> allPositionFromBothDB = new ArrayList<>();

        return this.webClient.get()
                .uri(getUri(position))
                .accept(MediaType.APPLICATION_JSON)
                .headers(headers -> headers.setBasicAuth(externalApiKey, ""))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(json -> processJsonResponse(json, allPositionFromBothDB));
    }

    private URI getUri(Position position) {
        return UriComponentsBuilder.newInstance()
                .path("/api/1.0/search")
                .queryParam("keywords", position.getTitle())
                .queryParam("locationName", position.getLocation())
                .queryParam("resultsToTake", "5")
                .build()
                .toUri();
    }

    private Mono<Object> processJsonResponse(String json, List<PositionListItem> allPositionFromBothDB) {
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
            return Mono.just(allPositionFromBothDB);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Mono.just(Collections.emptyList());
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

    //
//    Mono<String> response=webClient.get()
//            .uri(uriBuilder -> uriBuilder
//                    .path("/api/1.0/search")
//                    .queryParam("keywords",keyword.getTitle())
//                    .queryParam("locationName",keyword.getLocation())
//                    .queryParam("resultsToTake","20")
//                    .build())
//            .headers(headers -> headers.setBasicAuth(externalApiKey, ""))
//            .retrieve()
//            .bodyToMono(String.class);
//




//    public ReedJob(WebClient.Builder builder, @Value("reeds.url") String reedsUrl) {
//        this.webClient =builder
//                .baseUrl(reedsUrl)
//                .build();
//    }
//
//    public String getJob(){
//      ResponseEntity<String> response= webClient.get().
//                uri("/api/1.0/search")
//                .header("reeds.api.key")
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .toEntity(String.class)
//                .block();
//
//      return response.toString();
//    }
}
