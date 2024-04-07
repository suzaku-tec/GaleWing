package com.galewings.dto.ai.googleai.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SafetyRatingsDto implements Serializable {
    private String category;
    private String probability;

    private String blocked;

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

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
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