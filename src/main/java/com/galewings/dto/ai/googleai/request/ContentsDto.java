package com.galewings.dto.ai.googleai.request;

import java.io.Serializable;
import java.util.List;

public class ContentsDto implements Serializable {
    private List<PartsDto> parts;

    public void setParts(List<PartsDto> parts) {
        this.parts = parts;
    }

    public List<PartsDto> getParts() {
        return parts;
    }

    @Override
    public String toString() {
        return
                "ContentsDto{" +
                        "parts = '" + parts + '\'' +
                        "}";
    }
}