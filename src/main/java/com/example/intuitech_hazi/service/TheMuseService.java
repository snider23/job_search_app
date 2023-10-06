package com.example.intuitech_hazi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TheMuseService {

    private final String apiUrl = "https://www.themuse.com/api/public";

    @Value("${themuse.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Autowired
    public TheMuseService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public ResponseEntity<String> searchJobs(String query) {
        // Build the URL with query parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/jobs")
                .queryParam("api_key", apiKey)
                .queryParam("q", query);

        // Set headers if needed
        HttpHeaders headers = new HttpHeaders();

        // Make the API request
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                String.class
        );

        return response;
    }
}
