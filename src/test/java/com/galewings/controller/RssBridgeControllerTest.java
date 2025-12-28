package com.galewings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galewings.ModelMock;
import com.galewings.dto.RssBridgeKey;
import com.galewings.dto.input.ContentsListDto;
import com.galewings.dto.rssbridge.ItemsBean;
import com.galewings.dto.rssbridge.RssBridgeResponse;
import com.galewings.service.RssBridgeService;
import com.galewings.service.rssbridge.BlueskyBridgeService;
import com.galewings.service.rssbridge.InstagramBridgeService;
import com.galewings.service.rssbridge.RedditBridgeService;
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
    RedditBridgeService redditBridgeService;
    @Mock
    BlueskyBridgeService blueskyBridgeService;

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
        List<RssBridgeKey> keySelectList = (List<RssBridgeKey>) model.getAttribute("keySelect");

        Assertions.assertEquals("rssBridge", result);
        Assertions.assertEquals(1, connectSelectList.size());
        Assertions.assertEquals(1, keySelectList.size());
    }

    @Test
    void testInstagramContentsList() throws JsonProcessingException {
        RssBridgeResponse rssBridgeResponse = new RssBridgeResponse();
        ItemsBean itemsBean = new ItemsBean();
        itemsBean.title = "title";
        itemsBean.content_html = "html";
        rssBridgeResponse.items = List.of(itemsBean);
        when(instagramBridgeService.contentsList(any())).thenReturn(rssBridgeResponse);

        List<String> result = rssBridgeController.instagramContentsList(new ContentsListDto());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("html", result.get(0));
    }

    @Test
    void testRedditContentsList() throws JsonProcessingException {
        RssBridgeResponse rssBridgeResponse = new RssBridgeResponse();
        ItemsBean itemsBean = new ItemsBean();
        itemsBean.title = "title";
        itemsBean.content_html = "html";
        rssBridgeResponse.items = List.of(itemsBean);
        when(redditBridgeService.contentsList(any())).thenReturn(rssBridgeResponse);

        List<String> result = rssBridgeController.redditContentsList(new ContentsListDto());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("html", result.get(0));
    }

    @Test
    void testBlueskyContentsList() throws JsonProcessingException {
        RssBridgeResponse rssBridgeResponse = new RssBridgeResponse();
        ItemsBean itemsBean = new ItemsBean();
        itemsBean.title = "title";
        itemsBean.content_html = "html";
        rssBridgeResponse.items = List.of(itemsBean);
        when(blueskyBridgeService.contentsList(any())).thenReturn(rssBridgeResponse);

        List<String> result = rssBridgeController.blueskyContentsList(new ContentsListDto());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("html", result.get(0));
    }
}
