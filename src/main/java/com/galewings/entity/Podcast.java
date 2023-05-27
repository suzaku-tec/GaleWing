package com.galewings.entity;

import com.miragesql.miragesql.annotation.Column;

public class Podcast {
    @Column(name = "id")
    public String id;

    @Column(name = "url")
    public String url;
}
