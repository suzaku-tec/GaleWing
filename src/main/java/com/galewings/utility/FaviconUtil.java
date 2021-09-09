package com.galewings.utility;

import com.google.common.base.Strings;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import javax.imageio.ImageIO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class FaviconUtil {

  public Optional<BufferedImage> getFavigon(String url) throws MalformedURLException {

    if (!url.contains("favicon")) {
      throw new IllegalArgumentException("not contains favicon");
    }

    try {
      URL target = new URL(url);
      return Optional.of(ImageIO.read(target));
    } catch (MalformedURLException e) {
      throw e;
    } catch (IOException e) {
      return getFaviconForLinkTag(url);
    }
  }

  private Optional<BufferedImage> getFaviconForLinkTag(String url) {
    Document doc = null;
    try {
      doc = Jsoup.connect(url).get();
    } catch (IOException e) {
      return Optional.empty();
    }

    Elements faviconEl = doc.select("link[rel='shortcut icon']");
    if (faviconEl == null || faviconEl.size() <= 0) {
      faviconEl = doc.select("link[rel='icon']");
    }

    if (faviconEl == null || faviconEl.size() <= 0) {
      var favicon = faviconEl.stream().map(element -> element.attr("href"))
          .filter(s -> !Strings.isNullOrEmpty(s))
          .map(this::convertOptional)
          .map(Optional::orElseThrow)
          .findFirst().orElseThrow();

      try {
        return Optional.of(ImageIO.read(favicon));
      } catch (IOException e) {
        return Optional.empty();
      }
    }

    return Optional.empty();
  }

  private Optional<URL> convertOptional(String url) {
    try {
      return Optional.of(new URL(url));
    } catch (MalformedURLException e) {
      return Optional.empty();
    }
  }
}
