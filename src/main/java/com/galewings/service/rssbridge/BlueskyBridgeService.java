package com.galewings.service.rssbridge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.rssbridge.RssBridgeResponse;
import com.galewings.service.RssProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class BlueskyBridgeService {

    private final RestClient rssBridge;
    private final ObjectMapper objectMapper;
    private final RssProxyService rssProxyService;

    @Autowired
    public BlueskyBridgeService(ObjectMapper objectMapper,
                                @Value("${rsshub.base-url}") String baseUrl, RssProxyService rssProxyService) {
        this(objectMapper, RestClient.builder().baseUrl(baseUrl).build(), rssProxyService);
    }

    public BlueskyBridgeService(ObjectMapper objectMapper, RestClient rssBridge, RssProxyService rssProxyService) {
        this.objectMapper = objectMapper;
        this.rssBridge = rssBridge;
        this.rssProxyService = rssProxyService;
    }

    public RssBridgeResponse contentsList(String username) throws JsonProcessingException {
        String json = rssBridge.get()
                .uri("/?action=display&bridge=BlueskyBridge&data_source=getAuthorFeed&user_id=" + username + "&feed_filter=posts_and_author_threads&include_reposts=on&format=Json")
                .retrieve().body(String.class);

        RssBridgeResponse rssBridgeResponse = objectMapper.readValue(json, RssBridgeResponse.class);

        // プロキシ設定
        if (rssBridgeResponse != null && rssBridgeResponse.items != null) {
            rssBridgeResponse.items.forEach(itemsBean -> itemsBean.content_html = rssProxyService.convertUrlToProxy(itemsBean.content_html));
        }

        return rssBridgeResponse;
    }
}
