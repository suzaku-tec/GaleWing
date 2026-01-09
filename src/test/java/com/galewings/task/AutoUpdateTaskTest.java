package com.galewings.task;

import com.galewings.entity.Feed;
import com.galewings.entity.Site;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.SiteRepository;
import com.galewings.service.FeedFactoryService;
import com.galewings.service.GoogleAlertService;
import com.galewings.service.GwDateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AutoUpdateTaskTest {

    @Mock
    SiteRepository siteRepository;

    @Mock
    FeedRepository feedRepository;

    @Mock
    GwDateService gwDateService;

    @Mock
    private GoogleAlertService googleAlertService;

    @Mock
    private FeedFactoryService feedFactoryService;

    @InjectMocks
    AutoUpdateTask autoUpdateTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAllUpdate() {
        when(siteRepository.getAllSite()).thenReturn(List.of(new Site()));
        when(googleAlertService.isGoogleAlert(any())).thenReturn(false);
        when(feedRepository.existFeed(anyString())).thenReturn(true);

        autoUpdateTask.allUpdate();

        verify(siteRepository, times(2)).getAllSite();
    }

    @Test
    void testAllUpdate_FullFlow() {
        // 1. テストデータの準備
        Site site = new Site();
        site.uuid = "test-uuid";
        site.xmlUrl = "https://example.com/rss"; // 実際にはアクセスしないがURL形式が必要

        Feed mockFeed = new Feed();
        mockFeed.title = "正常な記事タイトル";
        mockFeed.link = "https://example.com/item/1";
        mockFeed.publishedDate = "2023-10-01";

        // 2. Mockの設定
        when(siteRepository.getAllSite()).thenReturn(List.of(site));
        when(googleAlertService.isGoogleAlert(any())).thenReturn(false);

        // FeedFactoryServiceのMock
        when(feedFactoryService.create(any(), anyString())).thenReturn(mockFeed);

        // 日付チェックと存在チェック
        when(gwDateService.isRetainedDateAfter(anyString())).thenReturn(true);
        when(feedRepository.existFeed(mockFeed.link)).thenReturn(false); // 新規記事として扱う
        when(gwDateService.now()).thenReturn(LocalDate.parse("2023-10-01"));

        // 3. 実行
        // 注意: 内部で `new XmlReader(new URL(site.xmlUrl))` が動くため、
        // 外部通信を避けるには本来 SyndFeedInput の Mock化が必要ですが、
        // 簡易的には site.xmlUrl にアクセス可能なURLを入れるか、
        // SyndFeedInput 自体を Factory 化して Mock するのが定石です。
        autoUpdateTask.allUpdate();

        // 4. 検証
        verify(googleAlertService, times(1)).updateFeed(any());
        verify(feedRepository, atLeastOnce()).insertEntity(any());
    }

    @Test
    void testAllUpdate_SkipPRFeed() {
        Site site = new Site();
        site.uuid = "test-uuid";
        site.xmlUrl = "https://example.com/rss";

        Feed prFeed = new Feed();
        prFeed.title = "PR：広告記事";

        when(siteRepository.getAllSite()).thenReturn(List.of(site));
        when(googleAlertService.isGoogleAlert(any())).thenReturn(false);
        when(feedFactoryService.create(any(), anyString())).thenReturn(prFeed);

        autoUpdateTask.allUpdate();

        // PR記事なので、DB保存は呼ばれないはず
        verify(feedRepository, never()).insertEntity(any());
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme