package com.galewings.controller;

import com.galewings.dto.input.TaskExecuteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@RequestMapping("/task")
@Controller
public class TaskExecuteController {

    @Autowired
    private Map<String, Runnable> taskMap;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("taskList", taskMap.keySet().stream().toList());
        return "task";
    }

    @ResponseBody
    public void execute(@RequestBody TaskExecuteDto dto) {
        taskMap.get(dto.getName()).run();
    }
}
