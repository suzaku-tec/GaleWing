package com.galewings.dto.ai.googleai.request;

import java.io.Serializable;

public class PartsDto implements Serializable {
    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return
                "PartsDto{" +
                        "text = '" + text + '\'' +
                        "}";
    }
}