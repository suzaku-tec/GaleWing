package com.galewings.config;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class NewsApiConfig {
    @Value("${newsapi.url:encoding=none}")
    private String baseUrl;

    @Bean
    RestClient customRestClient() {
        var connectionConfig = ConnectionConfig.custom()
                // TTLを設定
                .setTimeToLive(TimeValue.of(59, TimeUnit.SECONDS))
                // コネクションタイムアウト値を設定
                .setConnectTimeout(Timeout.of(1, TimeUnit.SECONDS))
                // ソケットタイムアウト値を設定（レスポンスタイムアウトと同義）
                .setSocketTimeout(Timeout.of(5, TimeUnit.SECONDS))
                .build();

        // PoolingHttpClientConnectionManagerを使うことでコネクションがプールされて
        // リクエストごとにコネクションを確立する必要がなくなる
        var connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setDefaultConnectionConfig(connectionConfig)
                // 全ルート合算の最大接続数
                .setMaxConnTotal(100)
                // ルート（基本的にはドメイン）ごとの最大接続数
                // !!! デフォルトが「5」で高負荷には耐えられない設定値なので注意 !!!
                .setMaxConnPerRoute(100)
                .build();

        var httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .build();

        var requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .defaultStatusHandler(new DefaultResponseErrorHandler())
                .build();
    }
}
