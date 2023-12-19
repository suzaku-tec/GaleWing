package com.galewings.controller;

import com.galewings.dto.CirculationDto;
import com.galewings.dto.input.CirculationAdd;
import com.galewings.repository.CategoryRepository;
import com.galewings.repository.CirculationRepository;
import com.galewings.service.GwDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/circulation")
@Controller
public class CirculationController {

    private enum Status {
        Application("application") // 申請
        , Approved("approved") // 承認
        , Rejection("rejection") // 却下
        , Reapplication("reapplication") // 再申請
        ;

        final String val;

        Status(String val) {
            this.val = val;
        }
    }

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GwDateService gwDateService;

    @Autowired
    private CirculationRepository circulationRepository;

    @PostMapping("add")
    @ResponseBody
    public int add(@RequestBody CirculationAdd circulationAdd) {
        CirculationDto dto = new CirculationDto();
        dto.id = UUID.randomUUID().toString();
        dto.link = circulationAdd.link;
        dto.title = circulationAdd.title;
        dto.createDate = gwDateService.now().toString();
        dto.status = Status.Application.val;
        return circulationRepository.insert(dto);
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("statusList", Status.values());
        model.addAttribute("circulationList", circulationRepository.selectAll());
        return "circulation";
    }

    @PostMapping("/list")
    @ResponseBody
    public List<CirculationDto> getCirculationList() {
        return circulationRepository.selectAll();
    }

    @PostMapping("/status/list")
    @ResponseBody
    public Status[] getStatusList() {
        return Status.values();
    }

}
