package com.galewings.entity;

import com.miragesql.miragesql.annotation.Column;

public class PodcastFeed {
    @Column(name = "id")
    public String id;

    @Column(name = "url")
    public String url;

    @Column(name = "text")
    public String text;
}
