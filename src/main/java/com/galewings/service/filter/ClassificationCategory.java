package com.galewings.service.filter;

import java.util.ArrayList;
import java.util.List;

public class ClassificationCategory {

    private String id;
    private List<ClassificationKeyword> keywords =
            new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ClassificationKeyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(
            List<ClassificationKeyword> keywords) {
        this.keywords = keywords;
    }
}