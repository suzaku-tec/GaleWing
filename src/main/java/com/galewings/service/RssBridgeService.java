package com.galewings.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.RssBridgeKey;
import com.galewings.dto.rssbridge.ItemsBean;
import com.galewings.repository.RssBridgeRepository;
import com.galewings.util.stream.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RssBridgeService {

    private final RssBridgeRepository rssBridgeRepository;

    private final RssProxyService rssProxyService;

    @Value("${rsshub.base-url}")
    private String rsshubBaseUrl;

    @Autowired
    public RssBridgeService(RssBridgeRepository rssBridgeRepository, RssProxyService rssProxyService) {
        this.rssBridgeRepository = rssBridgeRepository;
        this.rssProxyService = rssProxyService;
    }

    public List<String> selectIdList() {
        return rssBridgeRepository.selectIdList();
    }

    public List<String> getJsonList(String title) {

        ObjectMapper objectMapper = new ObjectMapper();
        return rssBridgeRepository.getJsonList(title)
                .stream()
                .map(json -> Result.runCatching(() -> objectMapper.readValue(json, ItemsBean.class)))
                .filter(Result::isSuccess)
                .map(Result::getOrNull)
                .peek(item -> item.content_html = rssProxyService.convertUrlToProxy(item.content_html))
                .map(rssBridgeResponse -> Result.runCatching(() -> objectMapper.writeValueAsString(rssBridgeResponse)))
                .filter(Result::isSuccess)
                .map(Result::getOrNull)
                .toList();
    }

    public List<RssBridgeKey> selectKeyList() {
        return rssBridgeRepository.selectKeyList();
    }

    /**
     * RSS-Hubのドメインで開始されているかチェックする
     *
     * @param url チェック対象URL
     * @return true:RSS-HUB false:RSS-HUB以外
     */
    public boolean isRssBridgeDomain(String url) {
        return url.startsWith(rsshubBaseUrl);
    }
}
