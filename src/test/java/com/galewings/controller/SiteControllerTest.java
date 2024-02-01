package com.galewings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galewings.ModelMock;
import com.galewings.dto.input.SiteDeleteDto;
import com.galewings.dto.input.SiteUpdateIconDto;
import com.galewings.dto.output.SiteLastFeedDto;
import com.galewings.entity.Site;
import com.galewings.repository.SiteRepository;
import com.galewings.service.URLService;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieStore;
import java.net.Proxy;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
        Elements els = doc.select("link[rel='icon']");
        Connection con = SiteControllerTest.getConnectionMockInstance(doc);
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
        Elements els = doc.select("link[rel='icon']");
        Connection con = SiteControllerTest.getConnectionMockInstance(doc);
        when(urlService.getUrlResourceAllByte(anyString())).thenReturn("A".getBytes());
        when(siteRepository.getSite(siteUpdateIconDto.getUuid())).thenReturn(site);
        try (MockedStatic<Jsoup> mocked = mockStatic(Jsoup.class)) {
            mocked.when(() -> Jsoup.connect(anyString())).thenReturn(con);

            // Act
            siteController.updateIcon(siteUpdateIconDto);
        }

    }

    public static Connection getConnectionMockInstance(Document doc) {
        return new MockResponse(doc);
    }

    static class MockResponse implements Connection {

        private final Document doc;

        public MockResponse(Document doc) {
            this.doc = doc;
        }

        @Override
        public Connection newRequest() {
            return null;
        }

        @Override
        public Connection url(URL url) {
            return null;
        }

        @Override
        public Connection url(String s) {
            return null;
        }

        @Override
        public Connection proxy(@Nullable Proxy proxy) {
            return null;
        }

        @Override
        public Connection proxy(String s, int i) {
            return null;
        }

        @Override
        public Connection userAgent(String s) {
            return null;
        }

        @Override
        public Connection timeout(int i) {
            return null;
        }

        @Override
        public Connection maxBodySize(int i) {
            return null;
        }

        @Override
        public Connection referrer(String s) {
            return null;
        }

        @Override
        public Connection followRedirects(boolean b) {
            return null;
        }

        @Override
        public Connection method(Method method) {
            return null;
        }

        @Override
        public Connection ignoreHttpErrors(boolean b) {
            return null;
        }

        @Override
        public Connection ignoreContentType(boolean b) {
            return null;
        }

        @Override
        public Connection sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            return null;
        }

        @Override
        public Connection data(String s, String s1) {
            return null;
        }

        @Override
        public Connection data(String s, String s1, InputStream inputStream) {
            return null;
        }

        @Override
        public Connection data(String s, String s1, InputStream inputStream, String s2) {
            return null;
        }

        @Override
        public Connection data(Collection<KeyVal> collection) {
            return null;
        }

        @Override
        public Connection data(Map<String, String> map) {
            return null;
        }

        @Override
        public Connection data(String... strings) {
            return null;
        }

        @Nullable
        @Override
        public KeyVal data(String s) {
            return null;
        }

        @Override
        public Connection requestBody(String s) {
            return null;
        }

        @Override
        public Connection header(String s, String s1) {
            return null;
        }

        @Override
        public Connection headers(Map<String, String> map) {
            return null;
        }

        @Override
        public Connection cookie(String s, String s1) {
            return null;
        }

        @Override
        public Connection cookies(Map<String, String> map) {
            return null;
        }

        @Override
        public Connection cookieStore(CookieStore cookieStore) {
            return null;
        }

        @Override
        public CookieStore cookieStore() {
            return null;
        }

        @Override
        public Connection parser(Parser parser) {
            return null;
        }

        @Override
        public Connection postDataCharset(String s) {
            return null;
        }

        @Override
        public Document get() throws IOException {
            return doc;
        }

        @Override
        public Document post() throws IOException {
            return null;
        }

        @Override
        public Response execute() throws IOException {
            return null;
        }

        @Override
        public Request request() {
            return null;
        }

        @Override
        public Connection request(Request request) {
            return null;
        }

        @Override
        public Response response() {
            return null;
        }

        @Override
        public Connection response(Response response) {
            return null;
        }
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme