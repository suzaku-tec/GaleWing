package com.galewings.service;

import com.galewings.exception.GaleWingsSystemException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;

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
}
