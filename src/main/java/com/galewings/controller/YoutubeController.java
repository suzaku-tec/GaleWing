package com.galewings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.YtdChannelDto;
import com.galewings.dto.YtdGridRendererDto;
import com.galewings.dto.output.YoutubeListSelectChannel;
import com.galewings.dto.youtube.videos.GridRendererItem;
import com.galewings.dto.youtube.videos.InitialData;
import com.galewings.repository.YoutubeRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@RequestMapping("youtube")
public class YoutubeController {

    @Autowired
    public YoutubeRepository youtubeRepository;

    @GetMapping("")
    public String index(Model model) {

        List<YtdChannelDto> list = channelList().stream().map(channel -> {
            try {
                YtdChannelDto ytdChannelListDto = new YtdChannelDto();
                ytdChannelListDto.channelId = channel.channelId;
                ytdChannelListDto.channelName = channel.name;
                ytdChannelListDto.ytdGridRendererDtos = videos(channel.channelId);
                return ytdChannelListDto;
            } catch (IOException e) {
                return null;
            }
        }).filter(ytdChannelDto -> ytdChannelDto != null).collect(Collectors.toList());

        model.addAttribute("ytdChannelList", list);

        return "/youtube/index";
    }

    public List<YtdGridRendererDto> videos(String cheannelId) throws IOException {

        Document document = Jsoup.connect(
                String.format("https://www.youtube.com/channel/%s/videos", cheannelId)).get();

        String regex = "\\{\"responseContext\":.*\\}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(document.html());

        if (m.find()) {
            ObjectMapper objectMapper = new ObjectMapper();
            InitialData initialData
                    = objectMapper.readValue(m.group(), InitialData.class);

            return initialData.contents.twoColumnBrowseResultsRenderer.tabs.get(
                            1).tabRenderer.content.sectionListRenderer.contents.get(
                            0).itemSectionRenderer.contents.get(0).gridRenderer.items.stream()
                    .filter(this::isNotEmptyThumbnails)
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

    private boolean isNotEmptyThumbnails(GridRendererItem item) {
        return ObjectUtils.isNotEmpty(item.gridVideoRenderer) &&
                ObjectUtils.isNotEmpty(item.gridVideoRenderer.videoId) &&
                ObjectUtils.isNotEmpty(item.gridVideoRenderer.title) &&
                ObjectUtils.isNotEmpty(item.gridVideoRenderer.title.runs) &&
                ObjectUtils.isNotEmpty(item.gridVideoRenderer.title.runs.get(0)) &&
                ObjectUtils.isNotEmpty(item.gridVideoRenderer.title.runs.get(0).text) &&
                ObjectUtils.isNotEmpty(item.gridVideoRenderer.thumbnail) &&
                ObjectUtils.isNotEmpty(item.gridVideoRenderer.thumbnail.thumbnails) &&
                ObjectUtils.isNotEmpty(item.gridVideoRenderer.thumbnail.thumbnails.get(0)) &&
                ObjectUtils.isNotEmpty(item.gridVideoRenderer.thumbnail.thumbnails.get(0).url);
    }

    @GetMapping("/channel/list")
    public List<YoutubeListSelectChannel> channelList() {
        return youtubeRepository.selectChannelList();
    }

}
