package com.galewings.dto.ai.googleai.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiResponseDto implements Serializable {
    private List<CandidatesDto> candidates;
    private PromptFeedbackDto promptFeedback;

    public void setCandidates(List<CandidatesDto> candidates) {
        this.candidates = candidates;
    }

    public List<CandidatesDto> getCandidates() {
        return candidates;
    }

    public void setPromptFeedback(PromptFeedbackDto promptFeedback) {
        this.promptFeedback = promptFeedback;
    }

    public PromptFeedbackDto getPromptFeedback() {
        return promptFeedback;
    }

    @Override
    public String toString() {
        return
                "GeminiResponseDto{" +
                        "candidates = '" + candidates + '\'' +
                        ",promptFeedback = '" + promptFeedback + '\'' +
                        "}";
    }
}