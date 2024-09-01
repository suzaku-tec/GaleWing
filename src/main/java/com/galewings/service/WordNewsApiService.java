package com.galewings.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.wordNews.response.WordNewsDto;
import com.galewings.exception.GaleWingsSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
public class WordNewsApiService {

    @Value("${wordNews.api-key}")
    private String apiKey;

    @Value("${word.news.api.url}")
    private String apiDomainUrl;

    private final GwDateService gwDateService;

    @Autowired
    public WordNewsApiService(GwDateService gwDateService) {
        this.gwDateService = gwDateService;
    }


    public WordNewsDto topNews(String country) throws IOException {

        if (Objects.isNull(apiKey)) {
            throw new GaleWingsSystemException("wordNews.api-key not found. add .env file");
        }

        String now = gwDateService.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        String sb = apiDomainUrl + "/top-news?source-country=" + country +
                "&language=ja&date=" + now;

        URL url = new URL(sb);
        HttpURLConnection connection = createConnection(url);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("x-api-key", apiKey);

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            ObjectMapper om = new ObjectMapper();
            return om.readValue(response.toString(), WordNewsDto.class);
        } else {
            throw new GaleWingsSystemException("GET request not worked");
        }
    }

    // `HttpURLConnection`を作成するファクトリーメソッド
    protected HttpURLConnection createConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }
}
