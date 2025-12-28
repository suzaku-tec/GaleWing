package com.galewings.service.rssbridge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.rssbridge.RssBridgeResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClient;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RedditBridgeServiceTest {
    @Mock
    RestClient rssBridge;
    @Mock
    ObjectMapper objectMapper;
    @InjectMocks
    RedditBridgeService redditBridgeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testContentsList() throws JsonProcessingException {
        RestClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec requestHeadersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(rssBridge.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn("json");

        RssBridgeResponse expectedResponse = new RssBridgeResponse();
        when(objectMapper.readValue("json", RssBridgeResponse.class)).thenReturn(expectedResponse);

        RssBridgeResponse result = redditBridgeService.contentsList("subReddit");
        Assertions.assertEquals(expectedResponse, result);
    }

    @Test
    void testConstructor() {
        RedditBridgeService service = new RedditBridgeService(objectMapper, "baseUrl");
        Assertions.assertNotNull(service);
    }
}
