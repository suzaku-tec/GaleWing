package com.galewings.task;

import com.galewings.dto.GaleWingSiteFeed;
import com.galewings.entity.Site;
import com.galewings.factory.FeedFactory;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.SiteRepository;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.net.URL;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoUpdateTask {

  /**
   * SiteRepository
   */
  @Autowired
  SiteRepository siteRepository;

  /**
   * FeedRepository
   */
  @Autowired
  FeedRepository feedRepository;

  @Scheduled(cron = "${update.scheduler.cron}")
  public void allUpdate() {
    siteRepository.getAllSite()
        .parallelStream()
        .map(site -> {
          return new GaleWingSiteFeed() {
            @Override
            public Site getSite() {
              return site;
            }

            @Override
            public Optional<SyndFeed> getOptionalSyndFeed() {
              Optional<SyndFeed> result;

              try {
                SyndFeed syndFeed = new SyndFeedInput().build(new XmlReader(new URL(site.xmlUrl)));
                result = Optional.ofNullable(syndFeed);
              } catch (Exception e) {
                result = Optional.empty();
              }

              return result;
            }
          };
        })
        .filter(siteFeed -> siteFeed.getOptionalSyndFeed().isPresent())
        .sequential()
        .forEach(siteFeed -> {
          siteFeed.getOptionalSyndFeed().get().getEntries().stream().filter(syndEntry -> {
            return !feedRepository.existFeed(syndEntry.getLink());
          }).map(syndEntry -> {
            return FeedFactory.create(syndEntry, siteFeed.getSite().uuid);
          }).forEach(feedRepository::insertEntity);
        });
  }
}
