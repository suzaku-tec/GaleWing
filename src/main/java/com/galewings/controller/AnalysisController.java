package com.galewings.controller;

import com.galewings.dto.input.AnalysisFeedAllReadDto;
import com.galewings.entity.Feed;
import com.galewings.repository.FeedRepository;
import com.galewings.service.GwJaroDistanceService;
import com.galewings.service.HotwordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController()
@RequestMapping("/analysis")
public class AnalysisController {

  private final HotwordService hotwordService;

  private final GwJaroDistanceService jaroDistanceService;

  private final FeedRepository feedRepository;

  @Autowired
  public AnalysisController(HotwordService hotwordService, GwJaroDistanceService jaroDistanceService, FeedRepository feedRepository) {
    this.hotwordService = hotwordService;
    this.jaroDistanceService = jaroDistanceService;
    this.feedRepository = feedRepository;
  }

  @GetMapping("/")
  public String index(@RequestParam(value = "targetLink", required = true) String targetLink,
                      Model model) throws UnsupportedEncodingException {
    Feed feed = feedRepository.selectFeedFor(URLDecoder.decode(targetLink, StandardCharsets.UTF_8));
    model.addAttribute("targetFeed", feed);
    return "analysis";
  }

  @GetMapping("/jaroWinklerDistance")
  public List<Feed> jaroWinklerDistance(
          @RequestParam(value = "targetTitle", required = true) String targetTitle) {
    return jaroDistanceService.resembleTitleForJaroWinkler(targetTitle);
  }

  @PostMapping("/feed/allRead")
  public boolean allRead(@RequestBody AnalysisFeedAllReadDto analysisFeedAllReadDto) {

    analysisFeedAllReadDto.links.stream().forEach(link -> {
      feedRepository.updateReadFeedNoOpen(link);
    });

    return true;
  }
}
