package com.galewings.service;

import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.any;
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
        ReflectionTestUtils.setField(minhonService, "name", "", String.class);
        ReflectionTestUtils.setField(minhonService, "key", "", String.class);
        ReflectionTestUtils.setField(minhonService, "secret", "", String.class);
    }


    @Test
    void testOauth() throws IOException {
        Response res = createOkHttpResponce("{access_token: test}");
        Call call = mock(Call.class);
        when(call.execute()).thenReturn(res);
        when(client.newCall(any())).thenReturn(call);
        String result = minhonService.oauth();
        Assertions.assertEquals("\"test\"", result);
    }

    @Test
    void testTranselate() throws IOException {
        // 翻訳部分のモック化
        Call call = mock(Call.class);
        when(client.newCall(any())).thenReturn(call);
        Response tranRes = createOkHttpResponce("{resultset: { result: { text: \"hello world\"} } }");
        Response oauthRes = createOkHttpResponce("{access_token: test}");
        when(call.execute()).thenReturn(oauthRes).thenReturn(tranRes);

        minhonService.oauth();
        String result = minhonService.transelate("text");
        Assertions.assertEquals("hello world", result);
    }

    @Test
    void testTranselate2() throws IOException {
        String result = minhonService.transelate("");
        Assertions.assertEquals("", result);
    }

    @Test
    void testTranselate3() throws IOException {

        // 翻訳部分のモック化
        Response res = createOkHttp510Responce("hello world");
        Call call = mock(Call.class);
        when(client.newCall(any())).thenReturn(call);
        Response tranRes = createOkHttpResponce("{resultset: { result: { text: \"hello world\"} } }");
        Response oauthRes = createOkHttpResponce("{access_token: test}");
        when(call.execute()).thenReturn(oauthRes).thenReturn(tranRes);

        minhonService.oauth();
        String result = minhonService.transelate("text");
        Assertions.assertEquals("hello world", result);
    }

    private Response createOkHttpResponce(String body) {
// 応答のステータスコードを定義
        int statusCode = 200;

// 応答のヘッダーを定義
        Headers headers = new Headers.Builder()
                .add("Content-Type", "application/json")
                .build();

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

    private Response createOkHttp510Responce(String resultText) {
// 応答のステータスコードを定義
        int statusCode = 510;

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