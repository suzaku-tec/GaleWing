package com.galewings.dto.ai.googleai.request;

import java.io.Serializable;
import java.util.List;

public class GeminiRequestDto implements Serializable {
    private List<ContentsDto> contents;

    public void setContents(List<ContentsDto> contents) {
        this.contents = contents;
    }

    public List<ContentsDto> getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return
                "GeminiDto{" +
                        "contents = '" + contents + '\'' +
                        "}";
    }
}