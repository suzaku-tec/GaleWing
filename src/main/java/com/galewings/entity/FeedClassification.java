package com.galewings.entity;

import com.miragesql.miragesql.annotation.Table;

/**
 * Feed分類結果を格納するエンティティ
 */
@Table(name = "feed_classification")
public class FeedClassification {
    private String feedLink;
    private String primaryCategory;
    private String categoriesJson;
    private String scoresJson;
    private String matchedRuleIdsJson;
    private String classifierVersion;
    private String classifiedAt;

    public String getScoresJson() {
        return scoresJson;
    }

    public void setScoresJson(String scoresJson) {
        this.scoresJson = scoresJson;
    }

    public String getFeedLink() {
        return feedLink;
    }

    public void setFeedLink(String feedLink) {
        this.feedLink = feedLink;
    }

    public String getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(String primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public String getCategoriesJson() {
        return categoriesJson;
    }

    public void setCategoriesJson(String categoriesJson) {
        this.categoriesJson = categoriesJson;
    }

    public String getMatchedRuleIdsJson() {
        return matchedRuleIdsJson;
    }

    public void setMatchedRuleIdsJson(String matchedRuleIdsJson) {
        this.matchedRuleIdsJson = matchedRuleIdsJson;
    }

    public String getClassifierVersion() {
        return classifierVersion;
    }

    public void setClassifierVersion(String classifierVersion) {
        this.classifierVersion = classifierVersion;
    }

    public String getClassifiedAt() {
        return classifiedAt;
    }

    public void setClassifiedAt(String classifiedAt) {
        this.classifiedAt = classifiedAt;
    }

}
