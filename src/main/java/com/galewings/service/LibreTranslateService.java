package com.galewings.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class LibreTranslateService {

    private static final Logger logger = LoggerFactory.getLogger(LibreTranslateService.class);

    /**
     * 翻訳
     *
     * @param text
     * @return
     */
    public String translate(String text) {

        try (HttpClient client = HttpClient.newHttpClient()) {


            String json = """
                    {"q":"%s","source":"en","target":"ja","format":"text"}
                    """.formatted(text);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:5000/translate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            logger.debug("LibreTranslate response: {}", response.body());

            if (!response.body().contains("translatedText")) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response.body());
            Thread.sleep(1000); // Wait for 1 second to avoid overwhelming the API
            return node.get("translatedText").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while translating text", e);
        }
    }
}
