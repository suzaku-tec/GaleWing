package com.galewings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.input.StackAddDto;
import com.galewings.entity.Stack;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.StackRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * StackController
 */
@RequestMapping("/stack")
@Controller
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
  @ResponseBody
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
  @ResponseBody
  public String addStack(@RequestBody StackAddDto dto) {
    stackRepository.addStack(dto.getUuid(), dto.getLink());

    feedRepository.updateReadFeed(dto.getLink());

    return Boolean.TRUE.toString();
  }

}
