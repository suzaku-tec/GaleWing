package com.galewings.service;

import com.galewings.dto.output.KeywordPrevDayComparisonDto;
import com.galewings.entity.Feed;
import com.galewings.repository.FeedKeywordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrendAnalyseService {

    private final FeedKeywordRepository feedKeywordRepository;

    public TrendAnalyseService(FeedKeywordRepository feedKeywordRepository) {
        this.feedKeywordRepository = feedKeywordRepository;
    }

    public List<KeywordPrevDayComparisonDto> searchDate(String date) {
        return feedKeywordRepository.selectKeywordPrevDayComparison(date);
    }

    public List<Feed> selectKeywordFeed(String keyword) {
        return this.feedKeywordRepository.selectKeywordFeed(keyword);
    }
}
