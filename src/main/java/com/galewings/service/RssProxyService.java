package com.galewings.service;

import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RssProxyService {

    private final String proxyBase = "https://localhost:8080/proxy/img?url=";  // 自サーバー

    public String convertUrlToProxy(String html) {

        // 正しい正規表現置換
        Pattern urlPattern = Pattern.compile(
                "https?://[\\w/:%#\\$&\\?\\(\\)~\\.=\\+\\-]+"
        );

        Matcher matcher = urlPattern.matcher(html);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String originalUrl = matcher.group();
            String encodedUrl = URLEncoder.encode(originalUrl, StandardCharsets.UTF_8);
            matcher.appendReplacement(sb, proxyBase + encodedUrl);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

}
