package com.galewings.controller;

import com.galewings.dto.input.RelationListDto;
import com.galewings.dto.relation.FeedRelation;
import com.galewings.service.RelationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/relation")
@Controller
@Transactional
public class RelationController {

    @Autowired
    private RelationService relationService;

    @PostMapping("/list")
    @ResponseBody
    public List<FeedRelation> list(@RequestBody RelationListDto relationListDto) {
        return relationService.list(relationListDto.uuid);
    }
}
