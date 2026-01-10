package com.galewings.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.SearxngResponse;
import com.galewings.dto.SearxngSearchResult;
import com.galewings.exception.GaleWingsSystemException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Service
public class SearchService {

    @Value("${searxng.base-url}")
    private String baseUrl;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public SearchService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<SearxngSearchResult> search(String query) {
        try {
            // クエリパラメータの構築（JSONフォーマットと日本語指定）
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = String.format("%s?q=%s&format=json&language=ja-JP", baseUrl, encodedQuery);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                SearxngResponse data = objectMapper.readValue(response.body(), SearxngResponse.class);
                return data.results != null ? data.results : Collections.emptyList();
            } else {
                throw new GaleWingsSystemException("Search request failed. status:" + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}