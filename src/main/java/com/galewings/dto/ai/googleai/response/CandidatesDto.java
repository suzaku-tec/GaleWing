package com.galewings.dto.ai.googleai.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CandidatesDto implements Serializable {
    private ContentDto content;
    private String finishReason;
    private int index;
    private List<SafetyRatingsDto> safetyRatings;

    public void setContent(ContentDto content) {
        this.content = content;
    }

    public ContentDto getContent() {
        return content;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setSafetyRatings(List<SafetyRatingsDto> safetyRatings) {
        this.safetyRatings = safetyRatings;
    }

    public List<SafetyRatingsDto> getSafetyRatings() {
        return safetyRatings;
    }

    @Override
    public String toString() {
        return
                "CandidatesDto{" +
                        "content = '" + content + '\'' +
                        ",finishReason = '" + finishReason + '\'' +
                        ",index = '" + index + '\'' +
                        ",safetyRatings = '" + safetyRatings + '\'' +
                        "}";
    }
}