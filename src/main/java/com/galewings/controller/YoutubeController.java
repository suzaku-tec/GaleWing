package com.galewings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.YtdGridRendererDto;
import com.galewings.dto.youtube.videos.InitialData;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("youtube")
public class YoutubeController {

  @GetMapping("")
  public String index() {
    return "/youtube/index";
  }

  @GetMapping("/videos")
  @ResponseBody
  public List<YtdGridRendererDto> videos() throws IOException {

    Document document = Jsoup.connect(
        "https://www.youtube.com/channel/UCJFZiqLMntJufDCHc6bQixg/videos").get();

    String regex = "\\{\"responseContext\":.*\\}";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(document.html());

    if (m.find()) {
      ObjectMapper objectMapper = new ObjectMapper();
      InitialData initialData
          = objectMapper.readValue(m.group(), InitialData.class);
      System.out.println(initialData);

      return initialData.contents.twoColumnBrowseResultsRenderer.tabs.get(
              1).tabRenderer.content.sectionListRenderer.contents.get(
              0).itemSectionRenderer.contents.get(0).gridRenderer.items.stream()
          .filter(item -> ObjectUtils.isNotEmpty(item.gridVideoRenderer))
          .filter(item -> ObjectUtils.isNotEmpty(item.gridVideoRenderer.videoId))
          .filter(
              gridRendererItem -> ObjectUtils.isNotEmpty(gridRendererItem.gridVideoRenderer.title))
          .filter(gridRendererItem -> ObjectUtils.isNotEmpty(
              gridRendererItem.gridVideoRenderer.title.runs))
          .filter(gridRendererItem -> ObjectUtils.isNotEmpty(
              gridRendererItem.gridVideoRenderer.title.runs.get(0)))
          .filter(gridRendererItem -> ObjectUtils.isNotEmpty(
              gridRendererItem.gridVideoRenderer.title.runs.get(0).text))
          .filter(gridRendererItem -> ObjectUtils.isNotEmpty(
              gridRendererItem.gridVideoRenderer.thumbnail))
          .filter(gridRendererItem -> ObjectUtils.isNotEmpty(
              gridRendererItem.gridVideoRenderer.thumbnail.thumbnails))
          .filter(gridRendererItem -> ObjectUtils.isNotEmpty(
              gridRendererItem.gridVideoRenderer.thumbnail.thumbnails.get(0)))
          .filter(gridRendererItem -> ObjectUtils.isNotEmpty(
              gridRendererItem.gridVideoRenderer.thumbnail.thumbnails.get(0).url))
          .map(item -> {
            YtdGridRendererDto ytdGridRendererDto = new YtdGridRendererDto();
            ytdGridRendererDto.videoId = item.gridVideoRenderer.videoId;
            ytdGridRendererDto.title = item.gridVideoRenderer.title.runs.get(0).text;
            ytdGridRendererDto.imgUrl = item.gridVideoRenderer.thumbnail.thumbnails.get(0).url;
            return ytdGridRendererDto;
          }).collect(Collectors.toList());
    }

    return Collections.emptyList();
  }
}
