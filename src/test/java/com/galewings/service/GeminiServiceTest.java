package com.galewings.service;

import com.galewings.dto.ai.googleai.response.GeminiResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class GeminiServiceTest {

    @InjectMocks
    private GeminiService geminiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
    void testTellMe_JsonProcessingException() throws IOException, InterruptedException {
        HttpClient mockClient = Mockito.mock(HttpClient.class);
        HttpResponse responseMock = Mockito.mock(HttpResponse.class);

        when(mockClient.send(any(), any())).thenReturn(responseMock);
        when(responseMock.body()).thenReturn("{ test");

        try (MockedStatic<HttpClient> mocked = mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newHttpClient).thenReturn(mockClient);

            GeminiResponseDto result = geminiService.tellMe("text");
            Assertions.fail();
        } catch (RuntimeException e) {
            // 正常
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void testTellMe_IOException() throws IOException, InterruptedException {
        HttpClient mockClient = Mockito.mock(HttpClient.class);
        HttpResponse responseMock = Mockito.mock(HttpResponse.class);

        // 例外設定
        when(mockClient.send(any(), any())).thenThrow(new IOException());
        when(responseMock.body()).thenThrow(new RuntimeException()); // これは実行されないはず

        try (MockedStatic<HttpClient> mocked = mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newHttpClient).thenReturn(mockClient);

            GeminiResponseDto result = geminiService.tellMe("text");
            Assertions.fail();
        } catch (RuntimeException e) {
            // 正常
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void testTellMe_InterruptedException() throws IOException, InterruptedException {
        HttpClient mockClient = Mockito.mock(HttpClient.class);
        HttpResponse responseMock = Mockito.mock(HttpResponse.class);

        // 例外設定
        when(mockClient.send(any(), any())).thenThrow(new InterruptedException());
        when(responseMock.body()).thenThrow(new RuntimeException()); // これは実行されないはず

        try (MockedStatic<HttpClient> mocked = mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newHttpClient).thenReturn(mockClient);

            GeminiResponseDto result = geminiService.tellMe("text");
            Assertions.fail();
        } catch (RuntimeException e) {
            // 正常
        } catch (Exception e) {
            Assertions.fail();
        }
    }

}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme