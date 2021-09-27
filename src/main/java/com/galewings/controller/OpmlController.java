package com.galewings.controller;

import be.ceau.opml.OpmlParseException;
import be.ceau.opml.OpmlParser;
import be.ceau.opml.entity.Opml;
import com.galewings.entity.Site;
import com.galewings.repository.SiteRepository;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RequestMapping("/opml")
@Controller
public class OpmlController {

  @Autowired
  TemplateEngine opmlTemplateEngine;

  @Autowired
  SiteRepository siteRepository;

  @RequestMapping(value = "/export", method = RequestMethod.GET)
  public ResponseEntity<byte[]> export() throws UnsupportedEncodingException {
    List<Site> allSite = siteRepository.getAllSite();

    Context context = new Context();

    List<Map<String, String>> collect = allSite.stream().map(Outline::new)
        .map(outline -> getFieldMap(outline))
        .collect(Collectors.toList());
    context.setVariable("outlines", collect);
    context.setVariable("title", "GaleWings");
    context.setVariable("date", LocalDateTime.now().format(
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));

    String opmlStr = opmlTemplateEngine.process("export", context);

    // ResponseHeader
    HttpHeaders header = new HttpHeaders();
    header.add("Content-Type", "text/opml");
    header.add("Content-Disposition",
        "attachment; filename*=utf-8''" + URLEncoder.encode("export.opml", StandardCharsets.UTF_8));
    return new ResponseEntity<>(opmlStr.getBytes(StandardCharsets.UTF_8), header, HttpStatus.OK);
  }

  @RequestMapping(value = "/import", method = RequestMethod.POST)
  @ResponseBody
  public void importOpml(@RequestParam("file") MultipartFile multipartFile)
      throws IOException, OpmlParseException {
    if (multipartFile.isEmpty()) {
      System.out.println("file is empty");
    }

    byte[] bytes = multipartFile.getBytes();
    Opml opml = new OpmlParser().parse(new String(bytes));
    opml.getBody().getOutlines().forEach(outline -> importOpml(outline));

  }

  private void importOpml(be.ceau.opml.entity.Outline outline) {
    if ("rss".equals(outline.getAttribute("type"))) {
      siteRepository.insertSite(outline);
    } else {
      outline.getSubElements().forEach(ol -> importOpml(ol));
    }
  }


  private class Outline {

    String title;
    String htmlUrl;
    String xmlUrl;

    public Outline(Site site) {
      title = site.title;
      htmlUrl = site.htmlUrl;
      xmlUrl = site.xmlUrl;
    }
  }

  private Map<String, String> getFieldMap(Object obj) {
    return Arrays.stream(obj.getClass().getDeclaredFields())
        .collect(Collectors.toMap(field -> field.getName(), field -> {
          try {
            return field.get(obj).toString();
          } catch (IllegalAccessException e) {
            return "";
          }
        }));
  }

}
