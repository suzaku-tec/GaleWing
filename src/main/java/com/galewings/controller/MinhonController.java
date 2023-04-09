package com.galewings.controller;

import com.galewings.service.MinhonService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/minhon")
public class MinhonController {

  @Autowired
  private MinhonService service;

  @ResponseBody
  @PostMapping("/transelate")
  public String transelate(@RequestParam("text") String text) throws IOException {
    return service.transelate(text);
  }

}
