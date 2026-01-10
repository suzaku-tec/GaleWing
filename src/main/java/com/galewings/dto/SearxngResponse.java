package com.galewings.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearxngResponse {
    public List<SearxngSearchResult> results;
}

