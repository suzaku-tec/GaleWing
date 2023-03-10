package com.galewings.task;

import com.galewings.dto.GaleWingSiteFeed;
import com.galewings.entity.Site;
import com.galewings.factory.FeedFactory;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.SiteRepository;
import com.galewings.service.GwDateService;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
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

  @Autowired
  GwDateService gwDateService;

  @Scheduled(cron = "${update.scheduler.cron}")
  public void allUpdate() {

    LocalDate retainedDate = gwDateService.retainedDate();

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
          siteFeed.getOptionalSyndFeed().get().getEntries().stream().map(syndEntry -> {
                return FeedFactory.create(syndEntry, siteFeed.getSite().uuid);
              }).filter(feed -> StringUtils.isNotBlank(feed.publishedDate))
              .filter(feed -> gwDateService.isRetainedDateAfter(feed.publishedDate))
              .filter(feed -> {
                return !feedRepository.existFeed(feed.link);
              })
              .forEach(feedRepository::insertEntity);

          siteRepository.updateFeedLastUpdateDate(siteFeed.getSite().uuid, gwDateService.now());
        });
  }
}
