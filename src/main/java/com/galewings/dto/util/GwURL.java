package com.galewings.dto.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class GwURL {

    private final URL url;

    public GwURL(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    public InputStream getInputStream() throws IOException {
        return this.getUrl().openConnection().getInputStream();
    }
}
