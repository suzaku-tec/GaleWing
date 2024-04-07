package com.galewings.dto.ai.googleai.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PromptFeedbackDto implements Serializable {
    private List<SafetyRatingsDto> safetyRatings;

    public void setSafetyRatings(List<SafetyRatingsDto> safetyRatings) {
        this.safetyRatings = safetyRatings;
    }

    public List<SafetyRatingsDto> getSafetyRatings() {
        return safetyRatings;
    }

    @Override
    public String toString() {
        return
                "PromptFeedbackDto{" +
                        "safetyRatings = '" + safetyRatings + '\'' +
                        "}";
    }
}