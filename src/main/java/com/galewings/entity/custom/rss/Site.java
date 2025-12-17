package com.galewings.entity.custom.rss;

import com.miragesql.miragesql.annotation.Column;

public class Site {

    public String url;

    @Column(name = "pageFilePath")
    public String pageFilePath;

    @Column(name = "uuid")
    public String uuid;
}
