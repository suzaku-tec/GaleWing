package com.galewings.dto;

public class ClassificationRuleDto {
    public String id;
    public String category;
    public String keyword;
    public int weight;

    public ClassificationRuleDto(String category, String id, String keyword, int weight) {
        this.category = category;
        this.id = id;
        this.keyword = keyword;
        this.weight = weight;
    }
}
