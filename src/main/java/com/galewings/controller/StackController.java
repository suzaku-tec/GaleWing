package com.galewings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.input.StackAddDto;
import com.galewings.entity.Stack;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.StackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * StackController
 */
@RequestMapping("/stack")
@RestController
public class StackController {

  /**
   * StackRepository
   */
  @Autowired
  StackRepository stackRepository;

  /**
   * FeedRepository
   */
  @Autowired
  FeedRepository feedRepository;

  @Autowired
  public StackController(StackRepository stackRepository, FeedRepository feedRepository) {
    this.stackRepository = stackRepository;
    this.feedRepository = feedRepository;
  }

  /**
   * 積読ページ初期表示
   *
   * @return 線先情報
   */
  @GetMapping("")
  public String index() {
    return "stack";
  }

  /**
   * 積読情報リスト取得
   *
   * @return 積読サイト情報リスト(JSON形式)
   * @throws JsonProcessingException
   */
  @GetMapping("/list")

  public String getStackList() throws JsonProcessingException {
    List<Stack> stackList = stackRepository.getStackList();

    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(stackList);
  }

  /**
   * 積読情報を追加
   *
   * @param dto 積読するサイト情報
   * @return 実行結果(true : 成功)
   */
  @PostMapping("/add")

  public String addStack(@RequestBody StackAddDto dto) {
    stackRepository.addStack(dto.getUuid(), dto.getLink());

    feedRepository.updateReadFeedNoOpen(dto.getLink());

    return Boolean.TRUE.toString();
  }

}
