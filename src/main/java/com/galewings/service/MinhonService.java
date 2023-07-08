package com.galewings.service;

import com.galewings.dto.Oauth2AccessToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
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
    private final String key = "";
    @Value("${minhon.client-secret}")
    private final String secret = "";
    @Value("${minhon.name}")
    private final String name = "";

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
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "client_credentials");
            params.add("client_id", key);
            params.add("client_secret", secret);
            ResponseEntity<Oauth2AccessToken> response = restTemplate.exchange(
                    CREDENTIALS_URL,
                    HttpMethod.POST,
                    new HttpEntity<>(params, null),
                    Oauth2AccessToken.class);

            String token = Optional.ofNullable(response.getBody())
                    .map(oauth2AccessToken -> oauth2AccessToken.access_token)
                    .orElse("");

            accessToken.set(token);
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
        MediaType mediaType = MediaType.parse("text/plain");
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
