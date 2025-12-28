package com.galewings.service.rssbridge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.rssbridge.RssBridgeResponse;
import com.galewings.service.RssProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Component
@Transactional
public class InstagramBridgeService {

    private final RestClient rssBridge;
    private final ObjectMapper objectMapper;
    private final RssProxyService rssProxyService;

    @Autowired
    public InstagramBridgeService(ObjectMapper objectMapper,
                                  RssProxyService rssProxyService,
                                  @Value("${rsshub.base-url:http://localhost:3000}") String baseUrl) {
        this(objectMapper, rssProxyService, RestClient.builder().baseUrl(baseUrl).build());
    }

    public InstagramBridgeService(ObjectMapper objectMapper, RssProxyService rssProxyService, RestClient rssBridge) {
        this.objectMapper = objectMapper;
        this.rssProxyService = rssProxyService;
        this.rssBridge = rssBridge;
    }

    public RssBridgeResponse contentsList(String username) throws JsonProcessingException {
        String json = rssBridge.get()
                .uri("/?action=display&bridge=InstagramBridge&context=Username&u=" + username + "&media_type=all&format=Json", username)
                .retrieve().body(String.class);

        RssBridgeResponse rssBridgeResponse = objectMapper.readValue(json, RssBridgeResponse.class);

        // プロキシ設定
        if (rssBridgeResponse != null && rssBridgeResponse.items != null) {
            rssBridgeResponse.items.forEach(itemsBean -> itemsBean.content_html = rssProxyService.convertUrlToProxy(itemsBean.content_html));
        }

        return rssBridgeResponse;
    }
}
