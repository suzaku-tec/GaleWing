package com.galewings.service.rssbridge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.rssbridge.ItemsBean;
import com.galewings.dto.rssbridge.RssBridgeResponse;
import com.galewings.service.RssProxyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BlueskyBridgeServiceTest {
    @Mock
    RestClient rssBridge;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    RssProxyService rssProxyService;
    @InjectMocks
    BlueskyBridgeService blueskyBridgeService;

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
        ItemsBean item = new ItemsBean();
        item.content_html = "html";
        expectedResponse.items = List.of(item);
        when(objectMapper.readValue("json", RssBridgeResponse.class)).thenReturn(expectedResponse);
        when(rssProxyService.convertUrlToProxy(anyString())).thenReturn("proxy");

        RssBridgeResponse result = blueskyBridgeService.contentsList("username");
        Assertions.assertEquals(expectedResponse, result);
        Assertions.assertEquals("proxy", result.items.get(0).content_html);
    }
}
