package com.galewings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galewings.dto.AddFeedDto;
import com.galewings.dto.ReadAllShowFeedDto;
import com.galewings.dto.ReadDto;
import com.galewings.dto.UpdateFeedDto;
import com.galewings.entity.Feed;
import com.galewings.entity.Site;
import com.galewings.entity.SiteFeedCount;
import com.galewings.entity.View;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.SiteRepository;
import com.galewings.repository.ViewsRepository;
import com.galewings.service.GoogleAlertService;
import com.galewings.service.URLService;
import com.galewings.task.AutoUpdateTask;
import com.rometools.rome.io.FeedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

class SiteFeedControllerTest {

    @Mock
    private SiteRepository siteRepository;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private URLService urlService;

    @Mock
    private AutoUpdateTask autoUpdateTask;

    @Mock
    private GoogleAlertService googleAlertService;

    @Mock
    private ViewsRepository viewsRepository;

    @InjectMocks
    private SiteFeedController siteFeedController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetFeedList() throws JsonProcessingException {
        when(feedRepository.getAllFeed()).thenReturn(List.of(new Feed()));
        when(feedRepository.getFeed(anyString())).thenReturn(List.of(new Feed()));

        String result = siteFeedController.getFeedList("uuid");
        assertEquals(
                "[{\"title\":null,\"uuid\":null,\"link\":null,\"uri\":null,\"author\":null,\"comments\":null,\"publishedDate\":null,\"opened\":false,\"readed\":false,\"imageUrl\":null,\"contentTerxt\":null}]",
                result);
    }

    @Test
    void testReadedFeed() throws UnsupportedEncodingException, JsonProcessingException {
        when(siteRepository.getSiteFeedCount(anyString())).thenReturn(
                List.of(new SiteFeedCount()));
        when(feedRepository.updateReadFeed(anyString())).thenReturn(0);

        String result = siteFeedController.readFeed(new ReadDto());
        assertEquals("[]", result);
    }

    @Test
    void testUpdateFeed() throws FeedException, IOException {
        when(siteRepository.getSite(anyString())).thenReturn(new Site());
        when(siteRepository.getAllSite()).thenReturn(List.of(new Site()));
        when(siteRepository.getSiteFeedCount()).thenReturn(
                List.of(new SiteFeedCount()));
        when(feedRepository.existFeed(anyString())).thenReturn(true);
        when(feedRepository.getAllFeed()).thenReturn(List.of(new Feed()));
        when(feedRepository.getFeed(anyString())).thenReturn(List.of(new Feed()));
        when(googleAlertService.isGoogleAlert(any())).thenReturn(false);

        String result = siteFeedController.updateFeed(new UpdateFeedDto());
        assertEquals(
                "{\"feeds\":[{\"title\":null,\"uuid\":null,\"link\":null,\"uri\":null,\"author\":null,\"comments\":null,\"publishedDate\":null,\"opened\":false,\"readed\":false,\"imageUrl\":null,\"contentTerxt\":null}],\"siteFeedCounts\":[{\"uuid\":null,\"title\":null,\"count\":0,\"faviconBase64\":null}]}",
                result);
    }

    @Test
    void testAddSiteFeedError() throws IOException {
        when(siteRepository.insertEntity(any())).thenReturn(0);

        try {
            siteFeedController.addSiteFeed(new AddFeedDto());
            fail("正常終了しちゃった余");
        } catch (IllegalArgumentException e) {
            assertEquals("不正なURLです", e.getMessage());
        }
    }

    @Test
    void testAddSiteFeed() throws IOException {
        when(siteRepository.insertEntity(any())).thenReturn(0);
        when(urlService.getUrlDomain(anyString())).thenReturn("http://127.0.0.1");

        AddFeedDto testDto = new AddFeedDto();
        testDto.setLink("http://127.0.0.1");

        siteFeedController.addSiteFeed(testDto);
    }

    @Test
    void testAddSiteFeed_rdf() throws IOException {
        when(siteRepository.insertEntity(any())).thenReturn(0);

        AddFeedDto testDto = new AddFeedDto();
        testDto.setLink("http://127.0.0.1/test.rdf");

        siteFeedController.addSiteFeed(testDto);
    }

    @Test
    void testReadAllShowFeed() throws JsonProcessingException {
        when(siteRepository.getSiteFeedCount()).thenReturn(
                List.of(new SiteFeedCount()));
        when(feedRepository.updateSiteFeedRead(anyString())).thenReturn(0);
        when(viewsRepository.info(anyString())).thenReturn(null);

        String result = siteFeedController.readAllShowFeed(new ReadAllShowFeedDto());
        assertEquals("[{\"uuid\":null,\"title\":null,\"count\":0,\"faviconBase64\":null}]",
                result);
    }

    @Test
    void testReadAllShowFeed2() throws JsonProcessingException {
        when(siteRepository.getSiteFeedCount()).thenReturn(
                List.of(new SiteFeedCount()));
        when(feedRepository.updateSiteFeedRead(anyString())).thenReturn(0);
        when(viewsRepository.info(anyString())).thenReturn(new View());

        String result = siteFeedController.readAllShowFeed(new ReadAllShowFeedDto());
        assertEquals("[{\"uuid\":null,\"title\":null,\"count\":0,\"faviconBase64\":null}]",
                result);
    }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme