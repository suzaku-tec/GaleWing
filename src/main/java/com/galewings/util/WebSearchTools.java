package com.galewings.util;

import com.galewings.dto.SearxngResponse;
import com.galewings.service.SearxngSearchService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class WebSearchTools {

    private final SearxngSearchService searchService;

    public WebSearchTools(SearxngSearchService searchService) {
        this.searchService = searchService;
    }

    @Tool(description = "SearXNGでWeb検索を行い、検索結果のタイトル、URL、概要を取得する")
    public SearxngResponse webSearch(String query) {
        return searchService.search(query);
    }
}
