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

@RequestMapping("/stack")
@Controller
public class StackController {

  @Autowired
  StackRepository stackRepository;

  @Autowired
  FeedRepository feedRepository;

  @GetMapping("")
  public String index() {
    return "stack";
  }

  @GetMapping("/list")
  @ResponseBody
  public String getStackList() throws JsonProcessingException {
    List<Stack> stackList = stackRepository.getStackList();

    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(stackList);
  }

  @PostMapping("/add")
  @ResponseBody
  public String addStack(@RequestBody StackAddDto dto) {
    stackRepository.addStack(dto.getUuid(), dto.getLink());

    feedRepository.updateReadFeed(dto.getLink());

    return Boolean.TRUE.toString();
  }

}
