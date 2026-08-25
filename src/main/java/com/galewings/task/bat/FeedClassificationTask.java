package com.galewings.task.bat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.entity.Feed;
import com.galewings.entity.FeedClassification;
import com.galewings.repository.FeedClassificationRepository;
import com.galewings.repository.FeedRepository;
import com.galewings.service.GwDateService;
import com.galewings.service.filter.ClassificationResult;
import com.galewings.service.filter.GwRuleBasedNewsClassifier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

record FeedClassificationSetRecord(Feed feed, ClassificationResult classificationResult) {
}

@Component
public class FeedClassificationTask implements Runnable {

    private final GwRuleBasedNewsClassifier gwRuleBasedNewsClassifier;

    private final FeedRepository feedRepository;

    private final FeedClassificationRepository feedClassificationRepository;

    private final GwDateService dateService;

    public FeedClassificationTask(GwRuleBasedNewsClassifier gwRuleBasedNewsClassifier, FeedRepository feedRepository, FeedClassificationRepository feedClassificationRepository, GwDateService dateService) {
        this.gwRuleBasedNewsClassifier = gwRuleBasedNewsClassifier;
        this.feedRepository = feedRepository;
        this.feedClassificationRepository = feedClassificationRepository;
        this.dateService = dateService;
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();

        Function<Set<?>, String> convertSetToJson = (Set<?> set) -> {
            try {
                return mapper.writeValueAsString(set);
            } catch (Exception e) {
                return StringUtils.EMPTY;
            }
        };

        Function<Map<String, ?>, String> convertMapToJson = (Map<String, ?> map) -> {
            try {
                return mapper.writeValueAsString(map);
            } catch (Exception e) {
                return StringUtils.EMPTY;
            }
        };

        Function<List<String>, String> convertListToJson = (List<String> list) -> {
            try {
                return mapper.writeValueAsString(list);
            } catch (Exception e) {
                return StringUtils.EMPTY;
            }
        };

        feedRepository.getAllFeed().stream()
                .filter(feed -> !feedClassificationRepository.existsClassification(feed.uuid) || gwRuleBasedNewsClassifier.getVersion().compareTo(feedClassificationRepository.getClassification(feed.uuid).getClassifierVersion()) > 0)
                .map(feed -> new FeedClassificationSetRecord(feed, gwRuleBasedNewsClassifier.classify(feed)))
                .map(result -> {
                    Feed feed = result.feed();
                    ClassificationResult classificationResult = result.classificationResult();
                    FeedClassification feedClassification = new FeedClassification();
                    feedClassification.setFeedLink(feed.link);
                    feedClassification.setPrimaryCategory(classificationResult.primaryCategory);
                    feedClassification.setCategoriesJson(convertSetToJson.apply(classificationResult.categories));
                    feedClassification.setScoresJson(convertMapToJson.apply(classificationResult.scores));
                    feedClassification.setMatchedRuleIdsJson(convertListToJson.apply(classificationResult.matchedRules));
                    feedClassification.setClassifierVersion(gwRuleBasedNewsClassifier.getVersion());
                    feedClassification.setClassifiedAt(dateService.now().format(GwDateService.DateFormat.SQLITE_DATE_FORMAT.dtf));
                    return feedClassification;
                })
                .forEach(feedClassificationRepository::mergeClassification);
    }
}
