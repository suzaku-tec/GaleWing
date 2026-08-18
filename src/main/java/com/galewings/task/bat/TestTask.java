package com.galewings.task.bat;

import com.galewings.repository.FeedRepository;
import com.galewings.service.filter.GwRuleBasedNewsClassifier;
import org.springframework.stereotype.Component;

@Component
public class TestTask implements Runnable {

    private final GwRuleBasedNewsClassifier gwRuleBasedNewsClassifier;

    private final FeedRepository feedRepository;

    public TestTask(GwRuleBasedNewsClassifier gwRuleBasedNewsClassifier, FeedRepository feedRepository) {
        this.gwRuleBasedNewsClassifier = gwRuleBasedNewsClassifier;
        this.feedRepository = feedRepository;
    }

    @Override
    public void run() {

        feedRepository.getFeed("8b4917cf-d8e9-4305-86c5-93a61d94f731").stream().limit(100)
                .forEach(feed -> {
                    var result = gwRuleBasedNewsClassifier.classify(feed);
                    System.out.println("Feed: " + feed.title + ", Category: " + result.primaryCategory + ", Confidence: " + result.confidence + ", Scores: " + result.scores);
                });
    }
}
