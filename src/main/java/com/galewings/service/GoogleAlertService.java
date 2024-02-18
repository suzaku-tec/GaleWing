package com.galewings.service;

import com.galewings.entity.Feed;
import com.galewings.entity.Site;
import com.galewings.repository.FeedRepository;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GoogleAlertService {

    @Autowired
    private GwDateService gwDateService;

    @Autowired
    private FeedRepository feedRepository;

    public boolean isGoogleAlert(Site site) {
        return StringUtils.contains(site.title, "site:http");
    }

    public void updateFeed(Site site) {
        if (!isGoogleAlert(site)) {
            return;
        }

        String url = site.xmlUrl;
        try {
            Document doc = Jsoup.connect(url).get();

            doc.select("entry")
                    .stream()
                    .map(element -> convert(site, element))
                    .filter(feed -> !feedRepository.existFeed(feed.link))
                    .forEach(feedRepository::insertEntity);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private Feed convert(Site site, Element element) {
        String id = element.getElementsByTag("id").first().text();
        String title = element.getElementsByTag("title").first().text();
        String link = element.getElementsByTag("link").first().attr("href");

        Feed feed = new Feed();
        feed.uuid = site.uuid;
        feed.title = title;
        feed.link = link;

        feed.publishedDate = gwDateService.now().toString();
        return feed;
    }
}
