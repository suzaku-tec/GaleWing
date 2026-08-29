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
import com.galewings.service.filter.ClassificationResult;
import com.galewings.service.filter.GwRuleBasedNewsClassifier;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 *
 */
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
    private FeedFactoryService feedFactoryService;

    @Autowired
    private GwRuleBasedNewsClassifier gwRuleBasedNewsClassifier;

    @Autowired
    private FeedClassificationRepository feedClassificationRepository;

    @Scheduled(cron = "${update.scheduler.cron}")
    public void allUpdate() {
        // Googleアラート用の更新
        siteRepository.getAllSite()
                .parallelStream()
                .filter(site -> googleAlertService.isGoogleAlert(site))
                .forEach(googleAlertService::updateFeed);

        siteRepository.getAllSite()
                .stream()
                .filter(site -> !googleAlertService.isGoogleAlert(site))
                .map(site -> new GaleWingSiteFeed() {
                    @Override
                    public Site getSite() {
                        return site;
                    }

                    @Override
                    public Optional<SyndFeed> getOptionalSyndFeed() {
                        Optional<SyndFeed> result;

                        try {
                            SyndFeed syndFeed = new SyndFeedInput().build(readUrlToXmlReader(URI.create(site.xmlUrl).toURL()));
                            result = Optional.ofNullable(syndFeed);
                        } catch (Exception e) {
                            result = Optional.empty();
                        }

                        return result;
                    }
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

    /**
     * Convert Set to JSON
     */
    private final Function<Set<?>, String> convertSetToJson = (Set<?> set) -> {
        try {
            return mapper.writeValueAsString(set);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    };

    /**
     * Convert Map to JSON
     */
    private final Function<Map<String, ?>, String> convertMapToJson = (Map<String, ?> map) -> {
        try {
            return mapper.writeValueAsString(map);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    };

    /**
     * Convert List to JSON
     */
    private final Function<List<String>, String> convertListToJson = (List<String> list) -> {
        try {
            return mapper.writeValueAsString(list);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    };

    /**
     * Insert feed classification
     *
     * @param feed Feed entity
     */
    private void insertFeedClassify(Feed feed) {
        try {
            ClassificationResult classificationResult = gwRuleBasedNewsClassifier.classify(feed);
            FeedClassification feedClassification = new FeedClassification();
            feedClassification.setFeedLink(feed.link);
            feedClassification.setPrimaryCategory(classificationResult.primaryCategory);
            feedClassification.setCategoriesJson(convertSetToJson.apply(classificationResult.categories));
            feedClassification.setScoresJson(convertMapToJson.apply(classificationResult.scores));
            feedClassification.setMatchedRuleIdsJson(convertListToJson.apply(classificationResult.matchedRules));
            feedClassification.setClassifierVersion(gwRuleBasedNewsClassifier.getVersion());
            feedClassification.setClassifiedAt(gwDateService.now().format(GwDateService.DateFormat.SQLITE_DATE_FORMAT.dtf));
            feedClassificationRepository.mergeClassification(feedClassification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Reader readUrlToXmlReader(URL url) throws IOException {
        return new BufferedReader(new InputStreamReader(url.openStream()));
    }


}
