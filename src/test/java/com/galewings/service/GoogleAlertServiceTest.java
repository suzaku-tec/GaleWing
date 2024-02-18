package com.galewings.service;

import com.galewings.entity.Site;
import com.galewings.repository.FeedRepository;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GoogleAlertServiceTest {
    @Mock
    GwDateService gwDateService;
    @Mock
    FeedRepository feedRepository;
    @InjectMocks
    GoogleAlertService googleAlertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsGoogleAlert() {
        boolean result = googleAlertService.isGoogleAlert(new Site());
        Assertions.assertFalse(result);
    }

    @Test
    void testUpdateFeed() {
        when(gwDateService.now()).thenReturn(LocalDate.of(2024, Month.FEBRUARY, 18));
        when(feedRepository.existFeed(anyString())).thenReturn(true);

        Document doc = Jsoup.parse("<entry><id>test</id><title>test</title><link>test</link></entry>");
        Connection con = JsoupTestMock.getConnectionMockInstance(doc);

        try (MockedStatic<Jsoup> mocked = mockStatic(Jsoup.class)) {
            mocked.when(() -> Jsoup.connect(anyString())).thenReturn(con);
            googleAlertService.updateFeed(new Site());
        }
    }

    @Test
    void testUpdateFeedRuntimeException() {
        when(gwDateService.now()).thenReturn(LocalDate.of(2024, Month.FEBRUARY, 18));
        when(feedRepository.existFeed(anyString())).thenThrow(new RuntimeException());

        Site site = new Site();
        site.title = "site:https";
        site.xmlUrl = StringUtils.EMPTY;

        Document doc = Jsoup.parse("<entry><id>test</id><title>test</title><link>test</link></entry>");
        Connection con = JsoupTestMock.getConnectionMockInstance(doc);

        try (MockedStatic<Jsoup> mocked = mockStatic(Jsoup.class)) {
            mocked.when(() -> Jsoup.connect(anyString())).thenReturn(con);
            googleAlertService.updateFeed(site);
            fail();
        } catch (RuntimeException e) {

        }
    }

    @Test
    void testUpdateFeedTrue() {
        when(gwDateService.now()).thenReturn(LocalDate.of(2024, Month.FEBRUARY, 18));
        when(feedRepository.existFeed(anyString())).thenReturn(false);
        when(feedRepository.insertEntity(any())).thenReturn(0);

        Site site = new Site();
        site.title = "site:https";
        site.xmlUrl = StringUtils.EMPTY;

        Document doc = Jsoup.parse("<entry><id>test</id><title>test</title><link>test</link></entry>");
        Connection con = JsoupTestMock.getConnectionMockInstance(doc);

        try (MockedStatic<Jsoup> mocked = mockStatic(Jsoup.class)) {
            mocked.when(() -> Jsoup.connect(anyString())).thenReturn(con);
            googleAlertService.updateFeed(site);
        }
    }


}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme