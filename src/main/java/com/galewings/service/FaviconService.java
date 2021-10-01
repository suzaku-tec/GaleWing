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

@Component
public class FaviconService {

  public Optional<String> getBase64Favicon(String url) {
    try {
      String domain = toDomain(url);
      URL favGet = new URL("http://www.google.com/s2/favicons?domain=" + domain);

      var img = ImageIO.read(favGet);
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
    }
  }

  private String toDomain(String url) throws URISyntaxException {
    URI uri = new URI(url);
    return uri.getRawAuthority();
  }
}
