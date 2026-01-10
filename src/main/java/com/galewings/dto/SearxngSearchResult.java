package com.galewings.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearxngSearchResult {
    public String title;
    public String url;
    public String content; // これがプロンプトに役立つスニペット
    public String engine;
}