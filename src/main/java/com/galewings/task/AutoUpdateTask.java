package com.galewings.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.GaleWingSiteFeed;
import com.galewings.entity.Feed;
import com.galewings.entity.FeedClassification;
import com.galewings.entity.Site;
import com.galewings.repository.FeedClassificationRepository;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.SiteRepository;
import com.galewings.service.FeedFactoryService;
import com.galewings.service.GoogleAlertService;
import com.galewings.service.GwDateService;
import com.galewings.service.async.TitleTagAnalysisAsyncService;
import com.galewings.service.filter.ClassificationResult;
import com.galewings.service.filter.GwRuleBasedNewsClassifier;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

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

    @Autowired
    private GoogleAlertService googleAlertService;

    @Autowired
    private TitleTagAnalysisAsyncService titleTagAnalysisAsyncService;

    @Autowired
    private FeedFactoryService feedFactoryService;

    @Autowired
    private GwRuleBasedNewsClassifier gwRuleBasedNewsClassifier;

    @Autowired
    private FeedClassificationRepository feedClassificationRepository;

    @Autowired
    private GwDateService dateService;

    @Scheduled(cron = "${update.scheduler.cron}")
    public void allUpdate() {
        // Googleアラート用の更新
        siteRepository.getAllSite()
                .parallelStream()
                .filter(site -> !googleAlertService.isGoogleAlert(site))
                .forEach(googleAlertService::updateFeed);

        siteRepository.getAllSite()
                .stream()
                .filter(site -> !googleAlertService.isGoogleAlert(site))
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
                .forEach(siteFeed -> {
                    siteFeed.getOptionalSyndFeed().get().getEntries()
                            .stream()
                            .map(syndEntry -> feedFactoryService.create(syndEntry, siteFeed.getSite().uuid))
                            .filter(feed -> !Strings.CS.startsWith(feed.title, "PR："))
                            .filter(feed -> StringUtils.isNotBlank(feed.publishedDate))
                            .filter(feed -> gwDateService.isRetainedDateAfter(feed.publishedDate))
                            .filter(feed -> StringUtils.isNotBlank(feed.link))
                            .filter(feed -> !feedRepository.existFeed(feed.link))
                            .forEach(feed -> {
                                feedRepository.insertEntity(feed);
                                insertFeedClassify(feed);
                            });

                    siteRepository.updateFeedLastUpdateDate(siteFeed.getSite().uuid, gwDateService.now());
                });
    }

    private final ObjectMapper mapper = new ObjectMapper();
    private final Function<Set<?>, String> convertSetToJson = (Set<?> set) -> {
        try {
            return mapper.writeValueAsString(set);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    };

    private final Function<Map<String, ?>, String> convertMapToJson = (Map<String, ?> map) -> {
        try {
            return mapper.writeValueAsString(map);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    };

    private final Function<List<String>, String> convertListToJson = (List<String> list) -> {
        try {
            return mapper.writeValueAsString(list);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    };

    private void insertFeedClassify(Feed feed) {
        // Implementation for inserting feed classification
        ClassificationResult classificationResult = gwRuleBasedNewsClassifier.classify(feed);
        FeedClassification feedClassification = new FeedClassification();
        feedClassification.setFeedLink(feed.link);
        feedClassification.setPrimaryCategory(classificationResult.primaryCategory);
        feedClassification.setCategoriesJson(convertSetToJson.apply(classificationResult.categories));
        feedClassification.setScoresJson(convertMapToJson.apply(classificationResult.scores));
        feedClassification.setMatchedRuleIdsJson(convertListToJson.apply(classificationResult.matchedRules));
        feedClassification.setClassifierVersion(gwRuleBasedNewsClassifier.getVersion());
        feedClassification.setClassifiedAt(dateService.now().format(GwDateService.DateFormat.SQLITE_DATE_FORMAT.dtf));
        feedClassificationRepository.mergeClassification(feedClassification);
    }

}
