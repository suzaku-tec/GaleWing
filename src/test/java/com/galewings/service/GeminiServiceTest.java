package com.galewings.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galewings.dto.ai.googleai.response.GeminiResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class GeminiServiceTest {
    GeminiService geminiService = new GeminiService();

    @Test
    void testTellMe() throws IOException, InterruptedException {
        HttpClient mockClient = Mockito.mock(HttpClient.class);
        HttpResponse responseMock = Mockito.mock(HttpResponse.class);

        when(mockClient.send(any(), any())).thenReturn(responseMock);
        when(responseMock.body()).thenReturn("{}");

        try (MockedStatic<HttpClient> mocked = mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newHttpClient).thenReturn(mockClient);

            GeminiResponseDto result = geminiService.tellMe("text");
            Assertions.assertNotNull(result);
        }
    }

    @Test
    void testTellMe_JsonProcessingException() {
        try (MockedStatic<HttpClient> mocked = mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newHttpClient).thenThrow(Mockito.mock(JsonProcessingException.class));
            GeminiResponseDto result = geminiService.tellMe("text");
            Assertions.fail();
        } catch (RuntimeException e) {
            // 正常
        }
    }

    @Test
    void testTellMe_IOException() {
        try (MockedStatic<HttpClient> mocked = mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newHttpClient).thenThrow(Mockito.mock(IOException.class));
            GeminiResponseDto result = geminiService.tellMe("text");
            Assertions.fail();
        } catch (RuntimeException e) {
            // 正常
        }
    }

    @Test
    void testTellMe_InterruptedException() {
        try (MockedStatic<HttpClient> mocked = mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newHttpClient).thenThrow(Mockito.mock(InterruptedException.class));
            GeminiResponseDto result = geminiService.tellMe("text");
            Assertions.fail();
        } catch (RuntimeException e) {
            // 正常
        }
    }

}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme