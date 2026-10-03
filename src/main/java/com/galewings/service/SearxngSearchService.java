package com.galewings.service;

import com.galewings.dto.SearxngResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SearxngSearchService {

    private final RestClient restClient;

    public SearxngSearchService(
            RestClient.Builder builder) {

        this.restClient = builder
                .baseUrl("http://localhost:18080")
                .build();
    }

    public SearxngResponse search(String query) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", query)
                        .queryParam("format", "json")
                        .queryParam("language", "ja")
                        .queryParam("safesearch", 1)
                        .queryParam("categories", "general")
                        .build())
                .retrieve()
                .body(SearxngResponse.class);
    }
}