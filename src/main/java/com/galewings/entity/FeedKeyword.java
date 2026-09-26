package com.galewings.entity;

import com.miragesql.miragesql.annotation.Column;
import com.miragesql.miragesql.annotation.Table;

@Table(name = "feed_keyword")
public class FeedKeyword {
    @Column(name = "feed_uuid")
    public String feedUuid;

    @Column(name = "feed_link")
    public String feedLink;

    @Column(name = "keyword")
    public String keywordStr;

    @Column(name = "normalized_keyword")
    public String normalizedKeyword;

    @Column(name = "part_of_speech")
    public String partOfSpeech;
}
