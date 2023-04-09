import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class Sample {

  public static void main(String[] args) throws Exception {
    RestTemplate restTemplate = new RestTemplate();

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "client_credentials");
    params.add("client_id", "582f7d3d822bb6fffe0230f84acdd5800641c4a1b");
    params.add("client_secret", "9414e86b471054e9991f0b8b88a1f87e");

    ResponseEntity<Oauth2AccessToken> response = restTemplate.exchange(
        "https://mt-auto-minhon-mlt.ucri.jgn-x.jp/oauth2/token.php",
        HttpMethod.POST,
        new HttpEntity<>(params, null),
        Oauth2AccessToken.class);

    transrate(response.getBody(), "おはようございます");

  }

  private static void transrate(Oauth2AccessToken oat, String str) throws IOException {
//    String url = "https://mt-auto-minhon-mlt.ucri.jgn-x.jp/api/";
//    RestTemplate restTemplate = new RestTemplate();
//
//    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//    params.add("access_token", body.access_token);
//    params.add("key", "582f7d3d822bb6fffe0230f84acdd5800641c4a1b");
//    params.add("api_name", "mt");
//    params.add("api_param", "generalNT_ja_en");
//    params.add("name", "suzaku380");
//    params.add("type", "json");
//    params.add("text", URLEncoder.encode(str, StandardCharsets.UTF_8));
//
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//    headers.set("charset", "utf-8");
//    headers.setAccept(List.of(MediaType.MULTIPART_FORM_DATA, MediaType.TEXT_PLAIN));
//    headers.set("Authorization", "Bearer e09d968b3a0e3b5fadae6f933b86f81ab3dbf0f8");
//
//    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
//        .queryParam("access_token", body.access_token)
//        .queryParam("key", "582f7d3d822bb6fffe0230f84acdd5800641c4a1b")
//        .queryParam("api_name", "mt")
//        .queryParam("api_param", "generalNT_ja_en")
//        .queryParam("name", "suzaku380")
//        .queryParam("type", "json")
//        .queryParam("text", URLEncoder.encode(str, StandardCharsets.UTF_8));
//
//    System.out.println("url:" + builder.build(true).toUriString());
//    String res = restTemplate.postForObject(
//        url,
//        new HttpEntity<>(params, headers),
//        String.class);
//
//    System.out.println(res);

    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    MediaType mediaType = MediaType.parse("text/plain");
    RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("key", "582f7d3d822bb6fffe0230f84acdd5800641c4a1b")
        .addFormDataPart("name", "suzaku380")
        .addFormDataPart("text", "おはようございます")
        .addFormDataPart("type", "json")
        .addFormDataPart("api_name", "mt")
        .addFormDataPart("api_param", "generalNT_ja_en")
        .addFormDataPart("access_token", oat.access_token)
        .build();
    Request request = new Request.Builder()
        .url("https://mt-auto-minhon-mlt.ucri.jgn-x.jp/api/")
        .method("POST", body)
//        .addHeader("Authorization", "Bearer e09d968b3a0e3b5fadae6f933b86f81ab3dbf0f8")
        .build();
    Response response = client.newCall(request).execute();

    JsonElement jsonObject = new JsonParser().parse(response.body().string()).getAsJsonObject();
    System.out.println(getValueForPath(jsonObject, "resultset.result.text"));
  }

  private static String getValueForPath(JsonElement jsonElement, String path) {
    String[] pathKeys = path.split("\\.");
    JsonObject obj = jsonElement.getAsJsonObject();
    for (int i = 0; i < pathKeys.length - 1; i++) {
      obj = obj.getAsJsonObject(pathKeys[i]);
    }

    return obj.get(pathKeys[pathKeys.length - 1]).getAsString();
  }
}

class Oauth2AccessToken {

  public String access_token;
  public Integer expires_in;
  public String token_type;
  public String scope;

  @Override
  public String toString() {
    return "Oauth2AccessToken{" +
        "access_token='" + access_token + '\'' +
        ", expires_in=" + expires_in +
        ", token_type='" + token_type + '\'' +
        ", scope='" + scope + '\'' +
        '}';
  }
}