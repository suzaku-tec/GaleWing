package com.galewings.task.bat;

import com.galewings.entity.Feed;
import com.galewings.entity.FeedKeyword;
import com.galewings.repository.FeedKeywordRepository;
import com.galewings.repository.FeedRepository;
import com.galewings.task.trend.TitleTokenizer;
import com.worksap.nlp.sudachi.Morpheme;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component
public class TrendAnalyse implements Runnable {

    private final TitleTokenizer titleTokenizer;

    private final FeedRepository feedRepository;

    private final FeedKeywordRepository feedKeywordRepository;

    public TrendAnalyse(TitleTokenizer titleTokenizer, FeedRepository feedRepository, FeedKeywordRepository feedKeywordRepository) {
        this.titleTokenizer = titleTokenizer;
        this.feedRepository = feedRepository;
        this.feedKeywordRepository = feedKeywordRepository;
    }

    @Override
    public void run() {
        var allFeeds = feedRepository.getAllFeed();


        allFeeds.stream()
                .filter(feed -> !feedKeywordRepository.isAnalysed(feed.uuid, feed.link))
                .map((feed) -> {
                    return new FeedAnalyseResult(titleTokenizer.extractKeywords(feed.title), feed);
                })
                .forEach(feedAnalysResult -> {
                    feedAnalysResult.keywords.stream().forEach((morpheme) -> {
                        FeedKeyword feedKeyword = new FeedKeyword();
                        feedKeyword.feedUuid = feedAnalysResult.feed.uuid;
                        feedKeyword.feedLink = feedAnalysResult.feed.link;
                        feedKeyword.keywordStr = morpheme.surface();
                        feedKeyword.normalizedKeyword = morpheme.normalizedForm();
                        feedKeyword.partOfSpeech = String.join("-", morpheme.partOfSpeech());
                        feedKeywordRepository.insertKeyword(feedKeyword);
                    });
                });
    }

    private record FeedAnalyseResult(List<Morpheme> keywords, Feed feed) {
    }
}
