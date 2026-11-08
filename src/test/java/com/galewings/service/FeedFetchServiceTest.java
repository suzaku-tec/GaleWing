package com.galewings.service;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedFetchServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private FeedFetchService feedFetchService;

    @BeforeEach
    void setUp() {
        feedFetchService = new FeedFetchService(restClient);
    }

    @Test
    @DisplayName("正常系：有効なRSS/AtomフィードXMLを取得してSyndFeedオブジェクトを返却できること")
    void testFetchFeed_Success() throws Exception {
        // 最小限の有効なRSS 2.0のXMLデータ
        String rssXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<rss version=\"2.0\">" +
                "<channel>" +
                "<title>Test Feed</title>" +
                "<link>https://example.com</link>" +
                "<description>Test Description</description>" +
                "</channel>" +
                "</rss>";

        InputStream inputStream = new ByteArrayInputStream(rssXml.getBytes());

        // RestClient のメソッドチェーンをモック化
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(InputStream.class)).thenReturn(inputStream);

        // テスト実行
        SyndFeed feed = feedFetchService.fetchFeed("https://example.com/rss");

        // 検証
        assertThat(feed).isNotNull();
        assertThat(feed.getTitle()).isEqualTo("Test Feed");
        assertThat(feed.getLink()).isEqualTo("https://example.com");
    }

    @Test
    @DisplayName("異常系：レスポンスのボディが null の場合に FeedException がスローされること")
    void testFetchFeed_BodyIsNull() {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(InputStream.class)).thenReturn(null);

        // 検証
        assertThatThrownBy(() -> feedFetchService.fetchFeed("https://example.com/rss"))
                .isInstanceOf(FeedException.class)
                .hasMessageContaining("Feed body is empty");
    }

    @Test
    @DisplayName("異常系：RestClient呼び出し中に例外が発生した場合に FeedException にラップされてスローされること")
    void testFetchFeed_RestClientException() {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenThrow(new RuntimeException("Connection error"));

        // 検証
        assertThatThrownBy(() -> feedFetchService.fetchFeed("https://example.com/rss"))
                .isInstanceOf(FeedException.class)
                .hasMessageContaining("Failed to fetch or parse feed")
                .hasRootCauseInstanceOf(RuntimeException.class);
    }
}
