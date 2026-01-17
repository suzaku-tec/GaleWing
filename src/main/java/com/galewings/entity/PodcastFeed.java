package com.galewings.entity;

import com.miragesql.miragesql.annotation.Column;

public class PodcastFeed {
    @Column(name = "podcast_id")
    public String id;

    @Column(name = "url")
    public String url;

    @Column(name = "text")
    public String text;


    @Column(name = "title")
    public String title;

    @Column(name = "read")
    public boolean read;

    @Column(name = "publishedDate")
    public String publishedDate;
}
