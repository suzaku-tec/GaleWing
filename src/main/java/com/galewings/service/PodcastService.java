package com.galewings.service;

import com.galewings.entity.Podcast;
import com.galewings.entity.PodcastFeed;
import com.galewings.repository.PodcastFeedRepository;
import com.galewings.repository.PodcastRepository;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PodcastService {

    @Autowired
    private PodcastRepository podcastRepository;

    @Autowired
    private PodcastFeedRepository podcastFeedRepository;

    @Autowired
    private GwDateService gwDateService;

    public void sync() {
        podcastRepository.selectAll().stream().map(podcast -> {
                    return getFeed(podcast.url);
                }).filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(syndFeed -> syndFeed.getEntries().stream())
                .filter(syndEntry -> podcastFeedRepository.isNotExist(syndEntry.getLink()))
                .map(syndEntry -> createPodcastFeed(syndEntry.getLink(), syndEntry.getTitle(), syndEntry.getPublishedDate()))
                .forEach(podcastFeedRepository::insert);
    }

    private Optional<SyndFeed> getFeed(String url) {
        try {
            SyndFeed syndFeed = new SyndFeedInput().build(new XmlReader(new URL(url)));
            return Optional.of(syndFeed);
        } catch (FeedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PodcastFeed createPodcastFeed(String url, String title, Date publishedDate) {
        PodcastFeed pf = new PodcastFeed();
        pf.id = UUID.randomUUID().toString();
        pf.url = url;
        pf.title = title;
        pf.publishedDate = GwDateService.DateFormat.DATE_TIME_COMMON.sdf.format(publishedDate);
        return pf;
    }

    public int addPodcast(String url, String title) {
        Podcast podcast = new Podcast();
        podcast.url = url;
        podcast.title = title;
        podcast.id = UUID.randomUUID().toString();
        return podcastRepository.insert(podcast);
    }

    public List<PodcastFeed> getNotReadFeed() {
        return podcastFeedRepository.selectAll();
    }

    public int markRead(String url) {
        return podcastFeedRepository.markRead(url);
    }
}
