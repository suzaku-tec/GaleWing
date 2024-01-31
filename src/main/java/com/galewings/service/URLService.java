package com.galewings.service;

import com.galewings.exception.GaleWingsSystemException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class URLService {

    private final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});

    public boolean validateUrl(String url) {
        return urlValidator.isValid(url);
    }

    public Optional<String> fixUrl(String domain, String url) {
        if (validateUrl(url)) {
            return Optional.of(url);
        }

        String newLink = appendPath(domain, url);
        if (validateUrl(newLink)) {
            return Optional.of(newLink);
        } else {
            return Optional.empty();
        }

    }

    public String getUrlDomain(String url) {
        Matcher m = Pattern.compile("(https?://[^/]+/)").matcher(url);
        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }
    }

    public String buildQueryString(String baseUrl, Map<String, String> params) {
        try {
            return buildQueryStringURI(baseUrl, params).toString();
        } catch (URISyntaxException e) {
            return StringUtils.EMPTY;
        }
    }

    public URI buildQueryStringURI(String baseUrl, Map<String, String> params) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(baseUrl);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            uriBuilder.setParameter(
                    entry.getKey(),
                    entry.getValue()
            );
        }

        return uriBuilder.build();
    }

    private String appendPath(String base, String addPath) {

        if (StringUtils.endsWith(base, "/")) {
            if (StringUtils.startsWith(addPath, "/")) {
                return base + StringUtils.substring(addPath, 1);
            } else {
                return base + addPath;
            }
        }

        if (!StringUtils.endsWith(base, "/")) {
            if (StringUtils.startsWith(addPath, "/")) {
                return base + addPath;
            } else {
                return base + "/" + addPath;
            }
        }

        throw new GaleWingsSystemException();
    }

    /**
     * URLの対象リソースを取得してバイト配列で返す。
     *
     * @param resourceUrl
     * @return
     * @throws IOException
     */
    public byte[] getUrlResourceAllByte(String resourceUrl) throws IOException {
        URL url = new URL(resourceUrl);
        return url.openConnection().getInputStream().readAllBytes();
    }
    
}
