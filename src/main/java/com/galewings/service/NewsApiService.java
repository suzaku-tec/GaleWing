package com.galewings.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.newsapi.request.OptionalRequestParam;
import com.galewings.dto.newsapi.response.NewsApiResponseDto;
import com.galewings.exception.GaleWingsSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class NewsApiService {
    @Value("${newsapi.api-key}")
    private String apiKey;

    private final RestClient restClient;

    @Autowired
    public NewsApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public NewsApiResponseDto topHeadlines() throws IOException {

        OptionalRequestParam optionalRequestParam = new OptionalRequestParam();
        optionalRequestParam.country = "us";

        return topHeadlines(optionalRequestParam);
    }

    public NewsApiResponseDto topHeadlines(OptionalRequestParam optionalRequestParam) throws IOException {
        if (Objects.isNull(apiKey)) {
            throw new GaleWingsSystemException("newsapi.api-key not found. add .env file");
        }

        Map<String, String> param = optionalRequestParam.queryParamMap();
        param.put("apiKey", apiKey);

        String paramStr = generateQueryParamStr(param);
        String response = restClient.get().uri("/top-headlines?" + paramStr).retrieve().body(String.class);
        ObjectMapper om = new ObjectMapper();

        return om.readValue(response, NewsApiResponseDto.class);
    }

    private String generateQueryParamStr(Map<String, ?> param) {
        return param.keySet().stream()
                .filter(key -> Objects.nonNull(param.get(key)))
                .map(key -> key + "=" + param.get(key))
                .collect(Collectors.joining("&"));
    }
}
