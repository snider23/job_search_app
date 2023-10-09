package com.example.intuitech_hazi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class ReedJob {

    private final WebClient webClient=WebClient.create("https://www.reed.co.uk");

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
