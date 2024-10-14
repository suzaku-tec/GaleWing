package com.galewings.dto.wordNews.response;

import java.io.IOException;

public enum Text {
    EMPTY;

    public String toValue() {
        switch (this) {
            case EMPTY:
                return "...";
        }
        return null;
    }

    public static Text forValue(String value) throws IOException {
        if (value.equals("...")) return EMPTY;
        throw new IOException("Cannot deserialize Text");
    }
}
