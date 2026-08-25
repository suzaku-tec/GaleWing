package com.galewings.service.filter;

import com.galewings.entity.Feed;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "news.classification")
@Component
public class GwRuleBasedNewsClassifier implements NewsClassifier {

    private String version;

    private List<ClassificationCategory> categories =
            new ArrayList<>();

    public List<ClassificationCategory> getCategories() {
        return categories;
    }

    public void setCategories(
            List<ClassificationCategory> categories) {
        this.categories = categories;
    }

    @Override
    public ClassificationResult classify(Feed feed) {
        String title = feed.title.toLowerCase();

        Map<String, Integer> scores = new HashMap<>();

        for (ClassificationCategory category : categories) {
            for (ClassificationKeyword keyword
                    : category.getKeywords()) {

                if (title.contains(keyword.getValue())) {
                    scores.merge(
                            category.getId(),
                            keyword.getWeight(),
                            Integer::sum
                    );
                }
            }
        }

        return ClassificationResult.from(scores);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}