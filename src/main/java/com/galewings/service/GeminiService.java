package com.galewings.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.GoogleSearch;
import com.google.genai.types.Tool;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

@Component
public class GeminiService {

    public String tellMe(String text) {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("GEMINI_API_KEY");

        try (Client client = Client.builder().apiKey(apiKey).build()) {

            GenerateContentConfig config = GenerateContentConfig.builder()
                    .tools(Tool.builder().googleSearch(GoogleSearch.builder().build()))
                    .build();

            GenerateContentResponse response =
                    client.models.generateContent(
                            "gemini-2.5-flash",
                            text,
                            config);

            return response.text();
        }

    }

}
