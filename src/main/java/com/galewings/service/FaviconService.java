package com.galewings.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Component;

/**
 * FaviconService
 * <p>
 * Googleのファビコン取得APIを利用してファビコン画像を取得する
 */
@Component
public class FaviconService {

  /**
   * 指定されたサイトのファビコンをbase64形式で取得する
   *
   * @param url URL
   * @return ファビコン画像のbase64形式の文字列
   */
  public Optional<String> getBase64Favicon(String url) {
    try {
      String domain = toDomain(url);
      URL favGet = new URL("http://www.google.com/s2/favicons?domain=" + domain);

      var img = ImageIO.read(favGet);

      if (img == null) {
        return Optional.empty();
      }

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(img, "png", baos);
      baos.flush();
      byte[] imageInByte = baos.toByteArray();
      baos.close();

      return Optional.of(Base64.getEncoder().encodeToString(imageInByte));

    } catch (URISyntaxException e) {
      e.printStackTrace();
      return Optional.empty();
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return Optional.empty();
    } catch (IOException e) {
      e.printStackTrace();
      return Optional.empty();
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  /**
   * リンクからドメイン情報を抽出する
   *
   * @param url URL
   * @return ドメインURL
   * @throws URISyntaxException
   */
  private String toDomain(String url) throws URISyntaxException {
    URI uri = new URI(url);
    return uri.getRawAuthority();
  }
}
