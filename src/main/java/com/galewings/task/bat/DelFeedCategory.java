package com.galewings.task.bat;

import com.galewings.repository.FeedTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DelFeedCategory implements Runnable {

    private final FeedTagRepository feedTagRepository;

    @Autowired
    public DelFeedCategory(FeedTagRepository feedTagRepository) {
        this.feedTagRepository = feedTagRepository;
    }

    @Scheduled(cron = "${update.feed.category.cron}")
    @Override
    public void run() {
        feedTagRepository.deleteLink();
    }
}
