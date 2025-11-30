package com.galewings.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OllamaClient {

    private final WebClient webClient;

    @Autowired
    public OllamaClient(WebClient ollamaWebClient) {
        this.webClient = ollamaWebClient;
    }

    public String generate(String prompt, String model) {
        Flux<String> responseFlux = webClient.post()
                .uri("/api/generate")
                .bodyValue(Map.of("model", model, "prompt", prompt))
                .retrieve()
                .bodyToFlux(String.class);
        ObjectMapper mapper = new ObjectMapper();
        String fullResponse = responseFlux
                .map(chunk -> {
                    try {
                        JsonNode root = mapper.readTree(chunk);
                        if (root.has("response")) {
                            return root.get("response").asText();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return "";
                })
                .collectList()
                .block() // ここで同期的に全文を取得
                .stream()
                .collect(Collectors.joining());

        return fullResponse != null ? fullResponse : "";
    }

}
