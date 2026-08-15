package com.galewings.service.filter;

public class ClassificationRule {
    public String id;
    public String category;
    public String keyword;
    public int weight;

    public ClassificationRule(String category, String id, String keyword, int weight) {
        this.category = category;
        this.id = id;
        this.keyword = keyword;
        this.weight = weight;
    }
}
