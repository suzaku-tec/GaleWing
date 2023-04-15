package com.galewings.controller;

import com.galewings.dto.input.TextDto;
import com.galewings.service.MinhonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/minhon")
public class MinhonController {

    @Autowired
    private MinhonService service;

    @ResponseBody
    @PostMapping("/transelate")
    public String transelate(@RequestBody TextDto dto) throws IOException {
        return service.transelate(dto.getText());
    }

}
