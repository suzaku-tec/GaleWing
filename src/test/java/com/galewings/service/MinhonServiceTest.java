package com.galewings.service;

import com.galewings.dto.Oauth2AccessToken;
import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MinhonServiceTest {
    @Mock
    AtomicReference<String> accessToken;
    @Mock
    Object lock;
    @Mock
    RestTemplate restTemplate;
    @Mock
    OkHttpClient client;
    @InjectMocks
    MinhonService minhonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOkHttpClient() {
        OkHttpClient result = minhonService.okHttpClient();
        Assertions.assertNotNull(result);
    }

    @Test
    void testOauth() {
        Oauth2AccessToken oat = new Oauth2AccessToken();
        oat.access_token = "test";
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(new ResponseEntity(oat, HttpStatus.OK));
        String result = minhonService.oauth();
        Assertions.assertEquals("test", result);
    }

    @Test
    void testTranselate() throws IOException {
        // Oauth部分のモック化
        Oauth2AccessToken oat = new Oauth2AccessToken();
        oat.access_token = "test";
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(new ResponseEntity(oat, HttpStatus.OK));

        // 翻訳部分のモック化
        Response res = createOkHttpResponce("hello world");
        Call call = mock(Call.class);
        when(call.execute()).thenReturn(res);
        when(client.newCall(any())).thenReturn(call);

        String result = minhonService.transelate("text");
        Assertions.assertEquals("hello world", result);
    }

    private Response createOkHttpResponce(String resultText) {
// 応答のステータスコードを定義
        int statusCode = 200;

// 応答のヘッダーを定義
        Headers headers = new Headers.Builder()
                .add("Content-Type", "application/json")
                .build();

// 応答のボディを定義
        String body = "{\"resultset\":{result:{text: \"" + resultText + "\"}}}";

// Responseオブジェクトを作成
        Response response = new Response.Builder()
                .code(statusCode)
                .headers(headers)
                .body(ResponseBody.create(MediaType.parse("application/json"), body))
                .message("OK")
                .request(new Request.Builder().url("http://example.com").build())
                .protocol(Protocol.HTTP_1_1)
                .build();
        return response;
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme