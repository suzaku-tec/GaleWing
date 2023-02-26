package com.galewings.task;

import com.galewings.repository.FeedRepository;
import com.galewings.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DelReadFeedTask {

  @Autowired
  SiteRepository siteRepository;

  @Autowired
  FeedRepository feedRepository;

  @Value("${days.retained}")
  private String daysRetained;

  @Scheduled(cron = "${readFeed.delete.scheduler.cron}")
  public void deleteReadFeed() {
    feedRepository.deleteReadFeed(daysRetained);
  }
}
