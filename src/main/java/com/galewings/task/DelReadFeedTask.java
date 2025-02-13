package com.galewings.task;

import com.galewings.repository.FeedRepository;
import com.galewings.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DelReadFeedTask {

  SiteRepository siteRepository;

  FeedRepository feedRepository;

  @Autowired
  public DelReadFeedTask(SiteRepository siteRepository, FeedRepository feedRepository) {
    this.siteRepository = siteRepository;
    this.feedRepository = feedRepository;
  }

  @Value("${days.retained}")
  private String daysRetained;

  @Scheduled(cron = "${readFeed.delete.scheduler.cron}")
  public int deleteReadFeed() {
    return feedRepository.deleteReadFeed(daysRetained);
  }
}
