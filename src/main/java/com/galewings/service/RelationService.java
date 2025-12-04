package com.galewings.service;

import com.galewings.dto.relation.FeedRelation;
import com.galewings.entity.FeedCategory;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.FeedTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class RelationService {

    private final FeedTagRepository feedTagRepository;
    private final FeedRepository feedRepository;

    @Autowired
    public RelationService(FeedTagRepository feedTagRepository, FeedRepository feedRepository) {
        this.feedTagRepository = feedTagRepository;
        this.feedRepository = feedRepository;
    }

    public List<FeedRelation> list(String link) {
        List<FeedCategory> categories = feedTagRepository.selectLink(link);
        List<String> tags = categories.stream().map(feedCategory -> feedCategory.tag).toList();

        if (tags.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> linkList = feedTagRepository.selectHighlyRelevantLink(tags);

        return linkList.stream().map(feedRepository::selectFeedFor).map(feed -> {
            FeedRelation feedRelation = new FeedRelation();
            feedRelation.title = feed.title;
            feedRelation.link = feed.link;
            return feedRelation;
        }).toList();

    }
}
