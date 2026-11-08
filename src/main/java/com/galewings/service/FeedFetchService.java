package com.galewings.service;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.InputStream;
import java.net.URI;

@Service
public class FeedFetchService {

    private final RestClient restClient;

    /**
     * コンストラクタ
     *
     * @param restClient RESTアクセス
     */
    public FeedFetchService(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * フィード情報取得
     *
     * @param feedUrl 取得URL
     * @return フィード情報
     * @throws FeedException フィード取得失敗
     */
    public SyndFeed fetchFeed(String feedUrl) throws FeedException {
        URI uri = URI.create(feedUrl);

        try (InputStream body = restClient.get()
                .uri(uri)
                .retrieve()
                .body(InputStream.class)) {

            if (body == null) {
                throw new FeedException("Feed body is empty: " + feedUrl);
            }

            return new SyndFeedInput().build(new XmlReader(body));
        } catch (FeedException e) {
            throw e;
        } catch (Exception e) {
            throw new FeedException("Failed to fetch or parse feed: " + feedUrl, e);
        }
    }
}
