package com.galewings.dto.ai.googleai.response;

import java.io.Serializable;

public class SafetyRatingsDto implements Serializable {
    private String category;
    private String probability;

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public String getProbability() {
        return probability;
    }

    @Override
    public String toString() {
        return
                "SafetyRatingsDto{" +
                        "category = '" + category + '\'' +
                        ",probability = '" + probability + '\'' +
                        "}";
    }
}