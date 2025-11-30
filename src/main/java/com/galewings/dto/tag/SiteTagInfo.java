package com.galewings.dto.tag;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SiteTagInfo {

    public String title;

    public TagInfo[] tags;

    @JsonProperty("primary_category")
    public String primaryCategory;
}

