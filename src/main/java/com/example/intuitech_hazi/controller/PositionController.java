package com.example.intuitech_hazi.controller;

import com.example.intuitech_hazi.domain.Position;
import com.example.intuitech_hazi.service.ClientService;
import com.example.intuitech_hazi.service.PositionService;
import com.example.intuitech_hazi.service.ReedJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
@RequestMapping("/positions")
@Validated
public class PositionController {

    private final String URL= "http://localhost:8080/positions/";

   private PositionService positionService;
   private ClientService clientService;

    private WebClient webClient;

//    public PositionController(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl("https://www.reed.co.uk").build();
//    }

    @Autowired
    public PositionController(PositionService positionService, ClientService clientService, WebClient.Builder webClientBuilder) {
        this.positionService = positionService;
        this.clientService = clientService;
        this.webClient = webClientBuilder.baseUrl("https://www.reed.co.uk")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .responseTimeout(Duration.ofMinutes(2)))) // Set response timeout to 10 minutes
                .build();
    }

    String username = "877fa464-345d-42ea-87bf-4975f7896c03";
    String password = "";  // Leave the password empty
    Charset charset = StandardCharsets.UTF_8;

    String encodedAuth = HttpHeaders.encodeBasicAuth(username, password, charset);

    @GetMapping("/jobs")        //TODO MEG KELL CSINALNI EZT AZ APIT: valahogy a username helyerekell csak az api de nem sikerul belerakni
    public Mono<String> getJobs(){
        return this.webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/api/1.0/search")
//                        .queryParam("keywords","developer")
//                        .queryParam("locationName","london")
//                        .queryParam("resultsToTake","20")
//                        .build())
//                .header(HttpHeaders.AUTHORIZATION,"877fa464-345d-42ea-87bf-4975f7896c03")
//                .retrieve()
//                .bodyToMono(String.class);
                .uri("/api/1.0/search?keywords=developer&locationName=london&resultsToTake=20")
               .header(HttpHeaders.AUTHORIZATION,"Basic"+encodedAuth)
                //.header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("apiKey:".getBytes()))
                .retrieve()
                .bodyToMono(String.class);
    }



    @PostMapping    //TODO nem hozza letre postmannel megnezni, headerken add at az apikeyt
    public ResponseEntity<String> createNewJob(@RequestBody Position newPosition,
                                               @RequestHeader("apiKey") String apiKey){
        if (!clientService.isApiKeyExists(apiKey)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Hibás API kulcs");
        }else {

            positionService.createNewPosition(newPosition);
//            newPosition.setTitle(newPosition.getTitle());
//            newPosition.setLocation(newPosition.getLocation());
            Long positionId = newPosition.getId();
            String positionUrl = URL + positionId;

            return ResponseEntity.status(HttpStatus.CREATED).body(positionUrl);
        }
    }

    @GetMapping
    public List<Position> getPositions(){
        return positionService.getPositions();

    }



    @GetMapping("/{id}")
    public Position getPositions(@PathVariable Long id){
        return positionService.getPositionById(id);
    }
}
