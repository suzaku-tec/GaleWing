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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @Mock
    private FeedFetchService feedFetchService;

    @Test
    void testGetFeedList() throws JsonProcessingException {
        when(feedRepository.getFeed(any())).thenReturn(List.of(new Feed()));
        when(functionCtrlRepository.get(any())).thenReturn(createFunctionCtrlMock("feed-img", "1"));

        List<Feed> result = siteFeedController.getFeedList("uuid");
        assertEquals(1, result.size());
        assertNull(result.getFirst().imageUrl);
    }

    @Test
    void testReadedFeed() throws JsonProcessingException {
        when(siteRepository.getSiteFeedCount(any())).thenReturn(
                List.of(new SiteFeedCount()));
        when(feedRepository.updateReadFeed(any())).thenReturn(0);

        String result = siteFeedController.readFeed(new ReadDto());
        assertEquals("[{\"uuid\":null,\"title\":null,\"count\":0,\"faviconBase64\":null}]", result);
    }

    @Test
    void testUpdateFeed() throws FeedException, IOException {
        when(siteRepository.getSiteFeedCount()).thenReturn(
                List.of(new SiteFeedCount()));
        when(feedRepository.getAllFeed()).thenReturn(List.of(new Feed()));
        doNothing().when(autoUpdateTask).allUpdate();

        String result = siteFeedController.updateFeed(new UpdateFeedDto());
        assertEquals(
                "{\"feeds\":[{\"title\":null,\"uuid\":null,\"link\":null,\"uri\":null,\"author\":null,\"comments\":null,\"publishedDate\":null,\"opened\":false,\"readed\":false,\"imageUrl\":null,\"contentTerxt\":null,\"translateTitle\":null}],\"siteFeedCounts\":[{\"uuid\":null,\"title\":null,\"count\":0,\"faviconBase64\":null}]}",
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
        when(mockFeed.getEntries()).thenReturn(List.of(entry)); // List<SyndEntry> testEntries準備
        when(siteRepository.getSite(any())).thenReturn(site);
        when(googleAlertService.isGoogleAlert(any())).thenReturn(false);
        when(feedRepository.existFeed(any())).thenReturn(false);
        when(feedFactoryService.create(any(), any())).thenReturn(f);
        when(gwDateService.isRetainedDateAfter(any())).thenReturn(true);
        when(titleTagAnalysAsyncService.asyncMethod(any(), any())).thenReturn(mock(CompletableFuture.class));
        when(feedRepository.insertEntity(any())).thenReturn(0);
        when(feedRepository.getFeed(any())).thenReturn(feeds);
        when(siteRepository.getSiteFeedCount()).thenReturn(siteFeedCounts);
        when(feedFetchService.fetchFeed(any())).thenReturn(mockFeed);

        String s = siteFeedController.updateFeed(dto);
        assertNotNull(s);

    }

    @Test
    void testAddSiteFeedError() {
        assertThrows(IllegalArgumentException.class, this::execute, "正常終了しちゃった余");
    }

    @Test
    void testAddSiteFeed() {
        when(rssBridgeService.isRssBridgeDomain(any())).thenReturn(true);

        AddFeedDto testDto = new AddFeedDto();
        testDto.setLink("http://127.0.0.1");

        siteFeedController.addSiteFeed(testDto);
    }

    @Test
    void testAddSiteFeed_rdf() {
        when(rssBridgeService.isRssBridgeDomain(any())).thenReturn(false);

        AddFeedDto testDto = new AddFeedDto();
        testDto.setLink("http://127.0.0.1/test.rdf");

        siteFeedController.addSiteFeed(testDto);

        verify(rssBridgeService).isRssBridgeDomain(any());
    }

    @Test
    void testAddSiteFeed_rss() {
        when(rssBridgeService.isRssBridgeDomain(any())).thenReturn(false);

        AddFeedDto testDto = new AddFeedDto();
        testDto.setLink("http://127.0.0.1/test.rss");

        siteFeedController.addSiteFeed(testDto);

        verify(rssBridgeService).isRssBridgeDomain(any());
    }

    @Test
    void testAddSiteFeed_rsslist() {
        when(rssBridgeService.isRssBridgeDomain(any())).thenReturn(false);

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
    void testAddSiteFeed_no_syndfeed() throws FeedException {
        when(rssBridgeService.isRssBridgeDomain(any())).thenReturn(false);
        when(urlService.getUrlDomain(any())).thenReturn("http://127.0.0.1");

        AddFeedDto testDto = new AddFeedDto();
        testDto.setLink("http://127.0.0.1/test.rsslist");

        when(feedFetchService.fetchFeed(any())).thenThrow(new RuntimeException("error"));

        siteFeedController.addSiteFeed(testDto);

        verify(urlService).getUrlDomain(any());
    }

    @Test
    void testAddSiteFeed_with_rss_links() throws IOException {
        when(rssBridgeService.isRssBridgeDomain(any())).thenReturn(false);
        when(urlService.getUrlDomain(any())).thenReturn("https://example.com");

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
            when(document.select("link[type*='atom+xml'],link[type*='rss+xml'],a[href^='/'],a[href^='" + any() + "']"))
                    .thenReturn(elements);
            jsoupMock.when(() -> Jsoup.connect(any())).thenReturn(connection);

            // urlService.fixUrlのモック（Optional<String>想定）
            when(urlService.fixUrl(eq("https://example.com"), eq("/rss.xml"))).thenReturn(Optional.of("https://example.com/rss.xml"));

            siteFeedController.addSiteFeed(testDto);

            // 検証：stream全分岐通過確認
            verify(urlService, times(2)).fixUrl(any(), any());  // linkとa両方
            verify(siteRepository, never()).countSiteForHtmlUrl(any());  // SyndFeed失敗でaddSiteAndFeed未実行
        }
    }


    @Test
    void testReadAllShowFeed() throws JsonProcessingException {
        when(siteRepository.getSiteFeedCount()).thenReturn(
                List.of(new SiteFeedCount()));
        when(feedRepository.updateSiteFeedRead(any())).thenReturn(0);
        when(viewsRepository.info(any())).thenReturn(null);

        String result = siteFeedController.readAllShowFeed(new ReadAllShowFeedDto());
        assertEquals("[{\"uuid\":null,\"title\":null,\"count\":0,\"faviconBase64\":null}]",
                result);
    }

    @Test
    void testReadAllShowFeed2() throws JsonProcessingException {
        when(viewsRepository.info(any())).thenReturn(new View());

        String result = siteFeedController.readAllShowFeed(new ReadAllShowFeedDto());
        assertEquals("[]", result);
    }

    @Test
    void testAddSiteFeed_NewSiteAndEntries_FullCoverage() throws FeedException {
        // 1. 準備：判定をバイパスするために .rss を使用
        String targetUrl = "https://example.com/feed.rss";
        AddFeedDto dto = new AddFeedDto();
        dto.setLink(targetUrl);

        // SyndFeedのモックを詳細に設定（SiteFactory.createで落ちないようにする）
        SyndFeed mockSyndFeed = mock(SyndFeed.class);
        when(mockSyndFeed.getLink()).thenReturn("https://example.com/home");
        when(mockSyndFeed.getTitle()).thenReturn("Example Title");

        // エントリの準備
        SyndEntry mockEntry = mock(SyndEntry.class);
        when(mockSyndFeed.getEntries()).thenReturn(List.of(mockEntry));

        // リポジトリとサービスのモック
        when(rssBridgeService.isRssBridgeDomain(any())).thenReturn(false);
        when(siteRepository.countSiteForHtmlUrl(any())).thenReturn(0);
        when(feedFactoryService.create(any(), any())).thenReturn(new Feed());

        when(feedFetchService.fetchFeed(any())).thenReturn(mockSyndFeed);

        // 3. 実行
        siteFeedController.addSiteFeed(dto);

        // 4. 検証
        // ここで失敗する場合、コンソール(System.err)にスタックトレースが出ているはずです
        verify(siteRepository, atLeastOnce()).insertEntity(any(Site.class));
        verify(feedRepository, atLeastOnce()).insertEntity(any(Feed.class));
    }

    @Test
    void testAddSiteFeed_AlreadyExists() {
        // 準備
        String targetUrl = "http://example.com/rss.xml";
        AddFeedDto dto = new AddFeedDto();
        dto.setLink(targetUrl);

        // 実行
        siteFeedController.addSiteFeed(dto);

        // 検証: countチェックのあと、insertは呼ばれないはず
        verify(siteRepository, never()).insertEntity(any(Site.class));
        verify(feedRepository, never()).insertEntity(any(Feed.class));
    }

    /**
     * FunctionCtrl作成
     *
     * @param id  ID
     * @param flg フラグ設定
     * @return FunctionCtrl
     */
    private FunctionCtrl createFunctionCtrlMock(String id, String flg) {
        FunctionCtrl functionCtrl = new FunctionCtrl();
        functionCtrl.id = id;
        functionCtrl.flg = flg;

        return functionCtrl;
    }

    private void execute() {
        siteFeedController.addSiteFeed(new AddFeedDto());

    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme