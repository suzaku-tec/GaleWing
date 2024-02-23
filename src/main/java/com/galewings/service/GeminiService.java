package com.galewings.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.ai.googleai.request.ContentsDto;
import com.galewings.dto.ai.googleai.request.GeminiRequestDto;
import com.galewings.dto.ai.googleai.request.PartsDto;
import com.galewings.dto.ai.googleai.response.GeminiResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class GeminiService implements TellMeAi<GeminiResponseDto> {

    @Value("{google.ai.api-key")
    private String apiKey;

    @Override
    public GeminiResponseDto tellMe(String text) {
        GeminiRequestDto dto = create(text);
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + apiKey))
                    .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(dto)))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            var res = client.send(req, HttpResponse.BodyHandlers.ofString());
            String json = res.body();
            ObjectMapper om = new ObjectMapper();
            GeminiResponseDto responseDto = om.readValue(json, GeminiResponseDto.class);
            return responseDto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private GeminiRequestDto create(String text) {
        GeminiRequestDto dto = new GeminiRequestDto();
        ContentsDto contentsDto = new ContentsDto();
        PartsDto partsDto = new PartsDto();
        partsDto.setText(text);

        dto.setContents(List.of(contentsDto));

        return dto;
    }
}
