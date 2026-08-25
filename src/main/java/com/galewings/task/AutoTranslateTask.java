package com.galewings.task;

import com.galewings.repository.FeedRepository;
import com.galewings.repository.SiteRepository;
import com.galewings.service.LibreTranslateService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoTranslateTask {

    private final SiteRepository siteRepository;

    private final FeedRepository feedRepository;

    private final LibreTranslateService libreTranslateService;

    public AutoTranslateTask(SiteRepository siteRepository, FeedRepository feedRepository, LibreTranslateService libreTranslateService) {
        this.siteRepository = siteRepository;
        this.feedRepository = feedRepository;
        this.libreTranslateService = libreTranslateService;
    }

    @Scheduled(cron = "${translate.scheduler.cron}")
    public void autoTranslate() {
        this.siteRepository
                .getAllSite()
                .stream()
                .filter(site -> site.translate)
                .flatMap(site -> this.feedRepository.getSiteFeed(site).stream())
                .map(feed -> {
                    feed.translateTitle = this.libreTranslateService.translate(feed.getTitle());
                    return feed;
                }).filter(feed -> feed.translateTitle != null && !feed.translateTitle.isEmpty())
                .forEach(feed -> this.feedRepository.updateTranslateTitle(feed));
    }
}
