package com.galewings.service.rssbridge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.rssbridge.RssBridgeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class RedditBridgeService {

    private final RestClient rssBridge;
    private final ObjectMapper objectMapper;

    @Autowired
    public RedditBridgeService(ObjectMapper objectMapper,
                               @Value("${rsshub.base-url}") String baseUrl) {
        this(objectMapper, RestClient.builder().baseUrl(baseUrl).build());
    }

    public RedditBridgeService(ObjectMapper objectMapper, RestClient rssBridge) {
        this.objectMapper = objectMapper;
        this.rssBridge = rssBridge;
    }

    public RssBridgeResponse contentsList(String subReddit) throws JsonProcessingException {
        String json = rssBridge.get()
                .uri("/?action=display&bridge=RedditBridge&context=single&r=" + subReddit + "&f=&score=&min_comments=-1&d=hot&t=week&search=&frontend=https%3A%2F%2Fold.reddit.com&format=Json")
                .retrieve().body(String.class);

        RssBridgeResponse rssBridgeResponse = objectMapper.readValue(json, RssBridgeResponse.class);

        return rssBridgeResponse;
    }
}
