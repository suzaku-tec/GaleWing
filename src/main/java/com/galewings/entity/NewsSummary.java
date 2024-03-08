package com.galewings.entity;

import com.miragesql.miragesql.annotation.Column;
import com.miragesql.miragesql.annotation.Table;

import java.io.Serializable;

@Table(name = "news_summary")
public class NewsSummary implements Serializable {

    @Column(name = "feed_uuid")
    public String feedUuid;

    @Column(name = "summary")
    public String summary;

    public String title;

    public String getFeedUuid() {
        return feedUuid;
    }

    public void setFeedUuid(String feedUuid) {
        this.feedUuid = feedUuid;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
