package com.galewings.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Component
public class MinhonService {

    private static final String BASE_URL = "https://mt-auto-minhon-mlt.ucri.jgn-x.jp/";

    private static final String CREDENTIALS_URL = BASE_URL + "oauth2/token.php";

    private static final String ENGINE_EN_JA = "generalNT_en_ja";
    private static final AtomicReference<String> accessToken = new AtomicReference<>();
    private static final Object lock = new Object();
    @Value("${minhon.client-id}")
    private String key;
    @Value("${minhon.client-secret}")
    private String secret;
    @Value("${minhon.name}")
    private String name;

    @Autowired
    OkHttpClient okHttpClient;


    @Autowired
    private RestTemplate restTemplate;

    private static String getValueForPath(JsonElement jsonElement, String path) {
        String[] pathKeys = path.split("\\.");
        JsonObject obj = jsonElement.getAsJsonObject();
        for (int i = 0; i < pathKeys.length - 1; i++) {
            obj = obj.getAsJsonObject(pathKeys[i]);
        }

        return obj.get(pathKeys[pathKeys.length - 1]).getAsString();
    }


    public String oauth() {
        synchronized (lock) {
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("grant_type", "client_credentials")
                    .addFormDataPart("client_id", key)
                    .addFormDataPart("client_secret", secret)
                    .build();
            Request request = new Request.Builder()
                    .url(CREDENTIALS_URL)
                    .method("POST", body)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                String token = JsonParser.parseString(Objects.requireNonNull(response.body()).string()).getAsJsonObject().get(
                        "access_token").toString();
                accessToken.set(token);
            } catch (IOException e) {
                // 何もしない
            }

        }

        return accessToken.get();
    }

    public String transelate(String text) throws IOException {
        if (StringUtils.isEmpty(text)) {
            return "";
        }

        if (accessToken.get() == null) {
            oauth();
        }

        // 翻訳
        Response response = generalNTJaEn(text);

        // 再認証 + 翻訳
        if (510 <= response.code() && response.code() <= 519) {
            oauth();
            response = generalNTJaEn(text);
        }

        JsonElement jsonObject = JsonParser.parseString(Objects.requireNonNull(response.body()).string()).getAsJsonObject();
        return getValueForPath(jsonObject, "resultset.result.text");
    }

    @NotNull
    private Response generalNTJaEn(String text) throws IOException {
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("key", key)
                .addFormDataPart("name", name)
                .addFormDataPart("text", text)
                .addFormDataPart("type", "json")
                .addFormDataPart("api_name", "mt")
                .addFormDataPart("api_param", ENGINE_EN_JA)
                .addFormDataPart("access_token", accessToken.get())
                .build();
        Request request = new Request.Builder()
                .url("https://mt-auto-minhon-mlt.ucri.jgn-x.jp/api/")
                .method("POST", body)
                .build();
        return okHttpClient.newCall(request).execute();
    }

}
