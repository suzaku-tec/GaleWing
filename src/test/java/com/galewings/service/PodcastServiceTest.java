package com.galewings.service;

import com.galewings.entity.Podcast;
import com.galewings.entity.PodcastFeed;
import com.galewings.repository.PodcastFeedRepository;
import com.galewings.repository.PodcastRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PodcastServiceTest {

    @InjectMocks
    @Spy
    private PodcastService podcastService;

    @Mock
    private PodcastRepository podcastRepository;

    @Mock
    private PodcastFeedRepository podcastFeedRepository;

    @Mock
    private GwDateService gwDateService;

    @Test
    @DisplayName("sync: 正常系 - getFeedをモック化して実行")
    void testSync_Success() throws Exception {
        // 1. Podcastの準備
        Podcast podcast = new Podcast();
        podcast.url = "http://example.com/rss";
        when(podcastRepository.selectAll()).thenReturn(List.of(podcast));

        // 2. 戻り値となる SyndFeed の準備
        SyndFeed mockFeed = new SyndFeedImpl();
        SyndEntry entry = new SyndEntryImpl();
        entry.setLink("http://example.com/item1");
        entry.setTitle("Item 1");
        entry.setPublishedDate(new Date());
        mockFeed.setEntries(List.of(entry));

        // 3. 重複チェックのモック
        when(podcastFeedRepository.isNotExist(anyString())).thenReturn(true);

        // 4. SyndFeedInputのコンストラクタをフックする
        try (MockedConstruction<XmlReader> mockedReader = mockConstruction(XmlReader.class);
             // 3. SyndFeedInputのコンストラクタをモック化
             MockedConstruction<SyndFeedInput> mockedInput = mockConstruction(SyndFeedInput.class,
                     (mock, context) -> {
                         // どんなXmlReaderが渡されてもmockFeedを返すように設定
                         when(mock.build(any(XmlReader.class))).thenReturn(mockFeed);
                     })) {
            // 5. 実行
            podcastService.sync();

            // 6. 検証
            verify(podcastFeedRepository, times(1)).insert(any(PodcastFeed.class));
        }
    }

    @Test
    @DisplayName("sync: 既知のフィードはスキップされること")
    void testSync_SkipDuplicate() {
        Podcast podcast = new Podcast();
        podcast.url = "http://example.com/rss";
        when(podcastRepository.selectAll()).thenReturn(List.of(podcast));

        SyndFeed mockFeed = new SyndFeedImpl();
        SyndEntry entry = new SyndEntryImpl();
        entry.setLink("http://example.com/item1");
        entry.setTitle("Item 1");
        entry.setPublishedDate(new Date());
        mockFeed.setEntries(List.of(entry));

        // 既に存在する場合はfalse
        when(podcastFeedRepository.isNotExist(anyString())).thenReturn(false);

        try (MockedConstruction<XmlReader> mockedReader = mockConstruction(XmlReader.class);
             // 3. SyndFeedInputのコンストラクタをモック化
             MockedConstruction<SyndFeedInput> mockedInput = mockConstruction(SyndFeedInput.class,
                     (mock, context) -> {
                         // どんなXmlReaderが渡されてもmockFeedを返すように設定
                         when(mock.build(any(XmlReader.class))).thenReturn(mockFeed);
                     })) {
            podcastService.sync();

            // insertが呼ばれないことを確認
            verify(podcastFeedRepository, never()).insert(any(PodcastFeed.class));
        }

    }

    @Test
    @DisplayName("createPodcastFeed: PodcastFeedオブジェクトが正しく生成されること")
    void testCreatePodcastFeed() {
        String url = "http://test.com";
        String title = "Title";
        Date now = new Date();

        PodcastFeed result = podcastService.createPodcastFeed(url, title, now);

        assertNotNull(result.id);
        assertEquals(url, result.url);
        assertEquals(title, result.title);
        assertNotNull(result.publishedDate);
    }

    @Test
    @DisplayName("addPodcast: Repositoryのinsertが呼ばれること")
    void testAddPodcast() {
        when(podcastRepository.insert(any(Podcast.class))).thenReturn(1);

        int result = podcastService.addPodcast("http://test.com", "Title");

        assertEquals(1, result);
        verify(podcastRepository, times(1)).insert(any(Podcast.class));
    }

    @Test
    @DisplayName("getNotReadFeed: 未読フィード一覧が取得できること")
    void testGetNotReadFeed() {
        List<PodcastFeed> list = List.of(new PodcastFeed());
        when(podcastFeedRepository.selectAll()).thenReturn(list);

        List<PodcastFeed> result = podcastService.getNotReadFeed();

        assertEquals(1, result.size());
        verify(podcastFeedRepository, times(1)).selectAll();
    }

    @Test
    @DisplayName("markRead: 既読処理が実行されること")
    void testMarkRead() {
        when(podcastFeedRepository.markRead("http://test.com")).thenReturn(1);

        int result = podcastService.markRead("http://test.com");

        assertEquals(1, result);
        verify(podcastFeedRepository, times(1)).markRead("http://test.com");
    }
}