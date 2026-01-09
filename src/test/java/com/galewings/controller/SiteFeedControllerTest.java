package com.galewings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galewings.dto.AddFeedDto;
import com.galewings.dto.ReadAllShowFeedDto;
import com.galewings.dto.ReadDto;
import com.galewings.dto.UpdateFeedDto;
import com.galewings.entity.*;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.FunctionCtrlRepository;
import com.galewings.repository.SiteRepository;
import com.galewings.repository.ViewsRepository;
import com.galewings.service.*;
import com.galewings.service.async.TitleTagAnalysisAsyncService;
import com.galewings.task.AutoUpdateTask;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
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

    @Mock
    private FunctionCtrlRepository functionCtrlRepository;

    @Mock
    private FeedFactoryService feedFactoryService;

    @Mock
    private GwDateService gwDateService;

    @Mock
    private RssBridgeService rssBridgeService;

    @Mock
    private TitleTagAnalysisAsyncService titleTagAnalysAsyncService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetFeedList() throws JsonProcessingException {
        when(feedRepository.getAllFeed()).thenReturn(List.of(new Feed()));
        when(feedRepository.getFeed(anyString())).thenReturn(List.of(new Feed()));
        when(functionCtrlRepository.get(any())).thenReturn(createFunctionCtrlMock("feed-img", "1"));

        List<Feed> result = siteFeedController.getFeedList("uuid");
        assertEquals(1, result.size());
        assertNull(result.getFirst().imageUrl);
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
    void testUpdateFeed_siteUpdate() throws FeedException, IOException {
        List<Feed> feeds = List.of(new Feed());
        List<SiteFeedCount> siteFeedCounts = List.of(new SiteFeedCount());
        Site site = new Site();
        site.xmlUrl = "http://localhost";

        UpdateFeedDto dto = new UpdateFeedDto();
        dto.setUuid("test");

        SyndEntry entry = mock(SyndEntry.class);
        SyndFeed mockFeed = mock(SyndFeed.class);
        Feed f = new Feed();
        f.publishedDate = "2026-01-01T00:00:00Z";
        try (MockedConstruction<XmlReader> xmlMock = mockConstruction(XmlReader.class,
                (mock, context) -> {
                    // XmlReaderモックは不要な処理をスキップ
                });
             MockedConstruction<SyndFeedInput> ignored = mockConstruction(SyndFeedInput.class,
                     (mock, context) -> {
                         when(mock.build(any(XmlReader.class))).thenReturn(mockFeed);
                     });
             MockedStatic<Jsoup> jsoupMock = Mockito.mockStatic(Jsoup.class, Mockito.CALLS_REAL_METHODS)) {
            when(mockFeed.getEntries()).thenReturn(List.of(entry)); // List<SyndEntry> testEntries準備
            when(siteRepository.getSite(anyString())).thenReturn(site);
            when(googleAlertService.isGoogleAlert(any())).thenReturn(false);
            when(feedRepository.existFeed(anyString())).thenReturn(false);
            when(feedFactoryService.create(any(), any())).thenReturn(f);
            when(gwDateService.isRetainedDateAfter(anyString())).thenReturn(true);
            when(titleTagAnalysAsyncService.asyncMethod(any(), any())).thenReturn(mock(CompletableFuture.class));
            when(feedRepository.insertEntity(any())).thenReturn(0);
            when(feedRepository.getFeed(anyString())).thenReturn(feeds);
            when(siteRepository.getSiteFeedCount()).thenReturn(siteFeedCounts);

            Connection connection = mock(Connection.class);
            Document document = mock(Document.class);
            Elements elements = new Elements();
            String link = "https://example.com";
            jsoupMock.when(() -> Jsoup.connect(link)).thenReturn(connection);
            when(connection.get()).thenReturn(document);
            when(document.select(anyString())).thenReturn(elements);

            String s = siteFeedController.updateFeed(dto);
            assertNotNull(s);
        }

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
        when(rssBridgeService.isRssBridgeDomain(anyString())).thenReturn(true);

        AddFeedDto testDto = new AddFeedDto();
        testDto.setLink("http://127.0.0.1");

        siteFeedController.addSiteFeed(testDto);
    }

    @Test
    void testAddSiteFeed_rdf() throws IOException {
        when(siteRepository.insertEntity(any())).thenReturn(0);
        when(rssBridgeService.isRssBridgeDomain(anyString())).thenReturn(false);

        AddFeedDto testDto = new AddFeedDto();
        testDto.setLink("http://127.0.0.1/test.rdf");

        siteFeedController.addSiteFeed(testDto);

        verify(rssBridgeService).isRssBridgeDomain(anyString());
    }

    @Test
    void testAddSiteFeed_rss() throws IOException {
        when(siteRepository.insertEntity(any())).thenReturn(0);
        when(rssBridgeService.isRssBridgeDomain(anyString())).thenReturn(false);

        AddFeedDto testDto = new AddFeedDto();
        testDto.setLink("http://127.0.0.1/test.rss");

        siteFeedController.addSiteFeed(testDto);

        verify(rssBridgeService).isRssBridgeDomain(anyString());
    }

    @Test
    void testAddSiteFeed_rsslist() throws IOException {
        when(siteRepository.insertEntity(any())).thenReturn(0);
        when(rssBridgeService.isRssBridgeDomain(anyString())).thenReturn(false);

        AddFeedDto testDto = new AddFeedDto();
        testDto.setLink("http://127.0.0.1/test.rsslist");

        try (MockedConstruction<SyndFeedInput> ignored = mockConstruction(SyndFeedInput.class,
                (mock, context) -> {
                    SyndFeed mockFeed = mock(SyndFeed.class);
                    when(mock.build(any(XmlReader.class))).thenReturn(mockFeed);
                })) {
            siteFeedController.addSiteFeed(testDto);

            assertEquals("http://127.0.0.1/test.rsslist", testDto.getLink());
        }
    }

    @Test
    void testAddSiteFeed_no_syndfeed() throws IOException {
        when(siteRepository.insertEntity(any())).thenReturn(0);
        when(rssBridgeService.isRssBridgeDomain(anyString())).thenReturn(false);
        when(urlService.getUrlDomain(anyString())).thenReturn("http://127.0.0.1");

        AddFeedDto testDto = new AddFeedDto();
        testDto.setLink("http://127.0.0.1/test.rsslist");

        try (MockedStatic<Jsoup> jsoupMock = Mockito.mockStatic(Jsoup.class);  // CALLS_REAL_METHODS削除
             MockedConstruction<SyndFeedInput> ignored = mockConstruction(SyndFeedInput.class,
                     (mock, context) -> when(mock.build(any(XmlReader.class))).thenThrow(new Exception("error")))) {

            Connection connection = mock(Connection.class);
            Document document = mock(Document.class);
            Elements elements = new Elements();

            // 最小モック
            when(connection.get()).thenReturn(document);
            when(document.select(anyString())).thenReturn(elements);
            jsoupMock.when(() -> Jsoup.connect(anyString())).thenReturn(connection);

            siteFeedController.addSiteFeed(testDto);

            verify(urlService).getUrlDomain(anyString());
        }
    }

    @Test
    void testAddSiteFeed_with_rss_links() throws IOException {
        when(siteRepository.insertEntity(any())).thenReturn(0);
        when(rssBridgeService.isRssBridgeDomain(anyString())).thenReturn(false);
        when(urlService.getUrlDomain(anyString())).thenReturn("https://example.com");

        AddFeedDto testDto = new AddFeedDto();
        testDto.setLink("https://example.com");

        try (MockedStatic<Jsoup> jsoupMock = Mockito.mockStatic(Jsoup.class);
             MockedConstruction<SyndFeedInput> ignored = mockConstruction(SyndFeedInput.class,
                     (mock, context) -> when(mock.build(any(XmlReader.class))).thenThrow(new Exception("error")))) {

            Connection connection = mock(Connection.class);
            Document document = mock(Document.class);

            // 非空Elements作成（stream通過用）
            Elements elements = new Elements();
            Element linkElement = new Element("link").attr("type", "application/rss+xml").attr("href", "/rss.xml");
            Element aElement = new Element("a").attr("href", "https://example.com/feed.atom");
            elements.add(linkElement);
            elements.add(aElement);

            when(connection.get()).thenReturn(document);
            when(document.select("link[type*='atom+xml'],link[type*='rss+xml'],a[href^='/'],a[href^='" + anyString() + "']"))
                    .thenReturn(elements);
            jsoupMock.when(() -> Jsoup.connect(anyString())).thenReturn(connection);

            // urlService.fixUrlのモック（Optional<String>想定）
            when(urlService.fixUrl(eq("https://example.com"), eq("/rss.xml"))).thenReturn(Optional.of("https://example.com/rss.xml"));

            siteFeedController.addSiteFeed(testDto);

            // 検証：stream全分岐通過確認
            verify(urlService, times(2)).fixUrl(anyString(), anyString());  // linkとa両方
            verify(siteRepository, never()).countSiteForHtmlUrl(anyString());  // SyndFeed失敗でaddSiteAndFeed未実行
        }
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

    private FunctionCtrl createFunctionCtrlMock(String id, String flg) {
        FunctionCtrl functionCtrl = new FunctionCtrl();
        functionCtrl.id = id;
        functionCtrl.flg = flg;

        return functionCtrl;
    }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme