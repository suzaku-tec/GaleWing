package com.example.galewings;

import com.example.galewings.dto.ReadiedDto;
import com.example.galewings.dto.UpdateFeedDto;
import com.example.galewings.entity.Feed;
import com.example.galewings.entity.Site;
import com.example.galewings.factory.FeedFactory;
import com.example.galewings.repository.FeedRepository;
import com.example.galewings.repository.SiteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class SiteFeedController {

    @Autowired
    SqlManager sqlManager;

    @Autowired
    SiteRepository siteRepository;

    @Autowired
    FeedRepository feedRepository;

    @GetMapping("/sitelist")
    @ResponseBody
    @Transactional
    public String getSiteList() throws JsonProcessingException {
        List<Site> resultList = sqlManager.getResultList(Site.class, new ClasspathSqlResource("sql/select_all_site.sql"));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(resultList);
    }

    @GetMapping("/feedlist")
    @ResponseBody
    @Transactional
    public String getFeedList(@RequestParam(value = "uuid", required = false) String uuid) throws JsonProcessingException {

        List<Feed> feeds;
        if (Strings.isNullOrEmpty(uuid)) {
            feeds = sqlManager.getResultList(Feed.class, new ClasspathSqlResource("sql/select_all_feed.sql"));
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("uuid", uuid);
            feeds = sqlManager.getResultList(Feed.class, new ClasspathSqlResource("sql/select_site_for_uuid.sql"), params);
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(feeds);
    }

    @PostMapping(value = "/readed")
    @ResponseBody
    @Transactional
    public String readedFeed(@RequestBody ReadiedDto dto) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        params.put("link", dto.getLink());
        sqlManager.executeUpdate(
                new ClasspathSqlResource("sql/update_feed_readed.sql")
                , params
        );
        return "";
    }

    @PostMapping(value = "/feed/update")
    @ResponseBody
    @Transactional
    public String updateFeed(@RequestBody UpdateFeedDto dto) throws IOException, FeedException {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

        List<Feed> feeds;
        if (Strings.isNullOrEmpty(dto.getUuid())) {
            siteRepository.getAllSite()
                    .stream()
                    .map(site -> {
                        return new SiteFeed() {
                            @Override
                            public Site getSite() {
                                return site;
                            }

                            @Override
                            public Optional<SyndFeed> getOptionalSyndFeed() {
                                return getSyndFeed(site.xmlUrl);
                            }
                        };
                    })
                    .filter(siteFeed -> siteFeed.getOptionalSyndFeed().isPresent())
                    .forEach(siteFeed -> {
                        siteFeed.getOptionalSyndFeed().get().getEntries().stream().filter(syndEntry -> {
                            return !feedRepository.existFeed(syndEntry.getLink());
                        }).peek(syndEntry -> System.out.println(syndEntry.getTitle() + ":" + syndEntry.getLink())).map(syndEntry -> {
                            return FeedFactory.create(syndEntry, siteFeed.getSite().uuid);
                        }).forEach(sqlManager::insertEntity);

                    });

            feeds = sqlManager.getResultList(Feed.class, new ClasspathSqlResource("sql/select_all_feed.sql"));
        } else {
            Site site = siteRepository.getSite(dto.getUuid());
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(site.xmlUrl)));
            feed.getEntries().stream().filter(syndEntry -> {
                return !feedRepository.existFeed(syndEntry.getLink());
            }).peek(syndEntry -> System.out.println(syndEntry.getTitle())).map(syndEntry -> {
                return FeedFactory.create(syndEntry, site.uuid);
            }).forEach(sqlManager::insertEntity);

            Map<String, String> params = new HashMap<>();
            params.put("uuid", site.uuid);
            feeds = sqlManager.getResultList(Feed.class, new ClasspathSqlResource("sql/select_site_for_uuid.sql"), params);
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(feeds);
    }

    private Optional<SyndFeed> getSyndFeed(String xmlUrl) {
        Optional<SyndFeed> result;

        try {
            SyndFeed syndFeed = new SyndFeedInput().build(new XmlReader(new URL(xmlUrl)));
            result = Optional.ofNullable(syndFeed);
        } catch (Exception e) {
            result = Optional.empty();
        }

        return result;
    }

    private interface SiteFeed {
        Site getSite();

        Optional<SyndFeed> getOptionalSyndFeed();
    }
}
