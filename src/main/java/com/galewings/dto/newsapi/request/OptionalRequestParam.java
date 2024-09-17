package com.galewings.dto.newsapi.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OptionalRequestParam {

    /**
     * The 2-letter ISO 3166-1 code of the country you want to get headlines for.
     * Possible options: us. Note: you can't mix this param with the sources param.
     */
    public String country;

    /**
     * The category you want to get headlines for. Possible options: businessentertainmentgeneralhealthsciencesportstechnology. Note: you can't mix this param with the sources param.
     */
    public String category;

    /**
     * A comma-seperated string of identifiers for the news sources or blogs you want headlines from. Use the /top-headlines/sources endpoint to locate these programmatically or look at the sources index. Note: you can't mix this param with the country or category params.
     */
    public String sources;

    /**
     * Keywords or a phrase to search for.
     */
    public String q;

    /**
     * The number of results to return per page (request). 20 is the default, 100 is the maximum.
     */
    public String pageSize;

    /**
     * Use this to page through the results if the total results found is greater than the page size.
     */
    public String page;

    public Map<String, String> queryParamMap() {
        Map<String, String> param = new HashMap<>();
        if (Objects.nonNull(country)) param.put("country", country);
        if (Objects.nonNull(category)) param.put("category", category);
        if (Objects.nonNull(sources)) param.put("sources", sources);
        if (Objects.nonNull(q)) param.put("q", q);
        if (Objects.nonNull(pageSize)) param.put("pageSize", pageSize);
        if (Objects.nonNull(page)) param.put("page", page);
        return param;
    }
}
