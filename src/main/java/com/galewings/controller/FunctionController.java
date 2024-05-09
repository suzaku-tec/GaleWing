package com.galewings.controller;

import com.galewings.entity.FunctionCtrl;
import com.galewings.repository.FunctionCtrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller()
@RequestMapping("/functionCtrl")
public class FunctionController {

    @Autowired
    private FunctionCtrlRepository functionCtrlRepository;

    @GetMapping("")
    public String index(Model model) {

        List<FunctionCtrl> functionCtrlList = functionCtrlRepository.getAll();
        model.addAttribute("functionCtrlList", functionCtrlList);

        return "functionCtrl";
    }

    @PostMapping("/update")
    @ResponseBody
    public void update(@RequestBody FunctionCtrl dto) {
        functionCtrlRepository.update(dto.id, dto.flg);
    }

}
