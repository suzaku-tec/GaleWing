package com.galewings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galewings.ModelMock;
import com.galewings.dto.input.SiteDeleteDto;
import com.galewings.dto.input.SiteUpdateIconDto;
import com.galewings.dto.output.SiteLastFeedDto;
import com.galewings.entity.Site;
import com.galewings.repository.SiteRepository;
import com.galewings.service.JsoupTestMock;
import com.galewings.service.URLService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SiteControllerTest {

    @Mock
    SiteRepository siteRepository;

    @Mock
    URLService urlService;

    @InjectMocks
    SiteController siteController;

    @Test
    void testGetSiteList() throws JsonProcessingException {
        when(siteRepository.getAllSite()).thenReturn(List.of(new Site()));

        String result = siteController.getSiteList();
        Assertions.assertEquals(
                "[{\"uuid\":null,\"title\":null,\"htmlUrl\":null,\"xmlUrl\":null,\"faviconBase64\":null,\"lastUpdate\":null,\"feedUpdateDate\":null}]",
                result);
    }

    @Test
    void testIndex() {
        when(siteRepository.getAllSite()).thenReturn(List.of(new Site()));

        ModelMock modelMock = new ModelMock();

        String result = siteController.index(modelMock);
        Assertions.assertEquals("/site/management", result);
        Assertions.assertEquals(1, modelMock.asMap().entrySet().size());
    }

    @Test
    void testDelete() {
        when(siteRepository.delete(anyString())).thenReturn(true);

        SiteDeleteDto param = new SiteDeleteDto();
        param.setUuid("");

        String result = siteController.delete(param);
        Assertions.assertEquals("true", result);
    }

    @Test
    void testFixLastUpdate() {
        Site testSiteData = new Site();
        testSiteData.uuid = "";
        testSiteData.lastUpdate = "";

        SiteLastFeedDto slfDto = new SiteLastFeedDto();
        slfDto.uuid = "";
        slfDto.lastUpdateDate = "";

        ModelMock modelMock = new ModelMock();

        when(siteRepository.getAllSite()).thenReturn(List.of(testSiteData));
        when(siteRepository.updateSiteLastUpdateDate(anyString(), anyString())).thenReturn(0);
        when(siteRepository.selectSiteLastFeed()).thenReturn(
                List.of(slfDto));

        String result = siteController.fixLastUpdate(modelMock);
        Assertions.assertEquals("/site/management", result);
        Assertions.assertEquals(1, modelMock.asMap().size());
    }

    @Test
    public void test_updateIcon_withFavicon() throws IOException {
        // Arrange
        SiteUpdateIconDto siteUpdateIconDto = new SiteUpdateIconDto();
        siteUpdateIconDto.setUuid("valid_uuid");

        Site site = new Site();
        site.htmlUrl = "https://example123123.com";

        Document doc = Jsoup.parse("<html><head><link rel='icon' href='http://example123123.com/favicon" +
                ".ico'></head></html>");
        Connection con = JsoupTestMock.getConnectionMockInstance(doc);
        when(urlService.getUrlResourceAllByte(anyString())).thenReturn("A".getBytes());
        when(siteRepository.getSite(siteUpdateIconDto.getUuid())).thenReturn(site);
        try (MockedStatic<Jsoup> mocked = mockStatic(Jsoup.class)) {
            mocked.when(() -> Jsoup.connect(anyString())).thenReturn(con);

            // Act
            siteController.updateIcon(siteUpdateIconDto);
        }

    }

    @Test
    public void test_updateIcon_withIcon() throws IOException {
        // Arrange
        SiteUpdateIconDto siteUpdateIconDto = new SiteUpdateIconDto();
        siteUpdateIconDto.setUuid("valid_uuid");

        Site site = new Site();
        site.htmlUrl = "https://example.com";

        Document doc = Jsoup.parse("<html><head><link href='favi.png'></head></html>");
        Connection con = JsoupTestMock.getConnectionMockInstance(doc);
        when(urlService.getUrlResourceAllByte(anyString())).thenReturn("A".getBytes());
        when(siteRepository.getSite(siteUpdateIconDto.getUuid())).thenReturn(site);
        try (MockedStatic<Jsoup> mocked = mockStatic(Jsoup.class)) {
            mocked.when(() -> Jsoup.connect(anyString())).thenReturn(con);

            // Act
            siteController.updateIcon(siteUpdateIconDto);
        }

    }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme