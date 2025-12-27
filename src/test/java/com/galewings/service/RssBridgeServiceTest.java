package com.galewings.service;

import com.galewings.dto.RssBridgeKey;
import com.galewings.repository.RssBridgeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class RssBridgeServiceTest {
    @Mock
    RssBridgeRepository rssBridgeRepository;
    @Mock
    RssProxyService rssProxyService;
    @InjectMocks
    RssBridgeService rssBridgeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSelectIdList() {
        when(rssBridgeRepository.selectIdList()).thenReturn(List.of("selectIdListResponse"));

        List<String> result = rssBridgeService.selectIdList();
        Assertions.assertEquals(List.of("selectIdListResponse"), result);
    }

    @Test
    void testGetJsonList() {
        when(rssBridgeRepository.getJsonList(anyString())).thenReturn(List.of("{id: 'test'}"));
        when(rssProxyService.convertUrlToProxy(anyString())).thenReturn("convertUrlToProxyResponse");

        List<String> result = rssBridgeService.getJsonList("title");
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testSelectKeyList() {
        RssBridgeKey rssBridgeKey = new RssBridgeKey();

        when(rssBridgeRepository.selectKeyList()).thenReturn(List.of(rssBridgeKey));

        List<RssBridgeKey> result = rssBridgeService.selectKeyList();
        Assertions.assertEquals(List.of(rssBridgeKey), result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme