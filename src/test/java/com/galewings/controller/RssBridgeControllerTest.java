package com.galewings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galewings.ModelMock;
import com.galewings.dto.RssBridgeKey;
import com.galewings.dto.input.ContentsListDto;
import com.galewings.dto.rssbridge.ItemsBean;
import com.galewings.dto.rssbridge.RssBridgeResponse;
import com.galewings.service.RssBridgeService;
import com.galewings.service.RssProxyService;
import com.galewings.service.rssbridge.InstagramBridgeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RssBridgeControllerTest {
    @Mock
    RssBridgeService rssBridgeService;
    @Mock
    InstagramBridgeService instagramBridgeService;
    @Mock
    RssProxyService rssProxyService;
    @InjectMocks
    RssBridgeController rssBridgeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndex() {
        when(rssBridgeService.selectKeyList()).thenReturn(List.of(new RssBridgeKey()));

        ModelMock model = new ModelMock();

        String result = rssBridgeController.index(model);

        List<String> connectSelectList = (List<String>) model.getAttribute("connectSelect");
        List<String> keySelectList = (List<String>) model.getAttribute("keySelect");

        Assertions.assertEquals("rssBridge", result);
        Assertions.assertEquals(1, connectSelectList.size());
        Assertions.assertEquals(1, keySelectList.size());
    }

    @Test
    void testInstagramContentsList() throws JsonProcessingException {
        RssBridgeResponse rssBridgeResponse = new RssBridgeResponse();
        ItemsBean itemsBean = new ItemsBean();
        itemsBean.title = "title";
        rssBridgeResponse.items = List.of(itemsBean);
        when(instagramBridgeService.contentsList(any())).thenReturn(rssBridgeResponse);

        List<String> result = rssBridgeController.instagramContentsList(new ContentsListDto());
        Assertions.assertEquals(1, result.size());
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme