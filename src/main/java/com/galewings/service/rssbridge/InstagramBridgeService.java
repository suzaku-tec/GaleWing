package com.galewings.service.rssbridge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.rssbridge.RssBridgeResponse;
import com.galewings.repository.RssBridgeRepository;
import com.galewings.service.RssProxyService;
import com.galewings.util.stream.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Component
@Transactional
public class InstagramBridgeService {

    private final RestClient rssBridge;
    private final ObjectMapper objectMapper;
    private final RssBridgeRepository rssBridgeRepository;
    private final RssProxyService rssProxyService;

    @Autowired
    public InstagramBridgeService(ObjectMapper objectMapper, RssBridgeRepository rssBridgeRepository, RssProxyService rssProxyService) {
        this.objectMapper = objectMapper;
        this.rssBridge = RestClient.builder().baseUrl("http://localhost:3000").build();
        this.rssBridgeRepository = rssBridgeRepository;
        this.rssProxyService = rssProxyService;
    }

    public void storeInstagramBridge(String username) throws JsonProcessingException {
        String json = rssBridge.get()
                .uri("/?action=display&bridge=InstagramBridge&context=Username&u=" + username + "&media_type=all&format=Json", username)
                .retrieve().body(String.class);

        RssBridgeResponse rssBridgeResponse = objectMapper.readValue(json, RssBridgeResponse.class);

        rssBridgeResponse.items.stream()
                .filter(itemsBean -> rssBridgeRepository.isExists(itemsBean.id) == 0)
                .map(itemsBean -> Result.runCatching(() -> objectMapper.writeValueAsString(itemsBean)))
                .filter(Result::isSuccess)
                .map(Result::getOrNull)
                .forEach(itemJson -> rssBridgeRepository.insert("InstagramBridge", itemJson, rssBridgeResponse.title));
    }

    public RssBridgeResponse contentsList(String username) throws JsonProcessingException {
        String json = rssBridge.get()
                .uri("/?action=display&bridge=InstagramBridge&context=Username&u=" + username + "&media_type=all&format=Json", username)
                .retrieve().body(String.class);

        RssBridgeResponse rssBridgeResponse = objectMapper.readValue(json, RssBridgeResponse.class);

        // プロキシ設定
        rssBridgeResponse.items.stream().peek(itemsBean -> itemsBean.content_html = rssProxyService.convertUrlToProxy(itemsBean.content_html));

        return rssBridgeResponse;
    }
}
