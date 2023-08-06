package com.galewings.controller;

import com.galewings.dto.input.ExecuteStatsSqlDto;
import com.galewings.dto.output.ExecuteStatsColumnDto;
import com.galewings.dto.output.ExecuteStatsResultDto;
import com.galewings.entity.Stats;
import com.galewings.repository.StatsRepository;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequestMapping("/stats")
@Controller
public class StatsController {

    @Autowired
    StatsRepository statsRepository;

    @PostMapping("/executeStatsSql")
    @ResponseBody
    public ExecuteStatsResultDto executeStatsSql(@RequestBody ExecuteStatsSqlDto dto) {
        Stats stats = statsRepository.getStats(dto.id);

        if (Strings.isNullOrEmpty(stats.sql)) {
            return null;
        }

        List<Map<String, Object>> ret = statsRepository.executeStatsSql(stats.sql);
        ExecuteStatsResultDto res = new ExecuteStatsResultDto();
        res.columns = transformColumnList(ret);
        res.values = transformValueList(ret);
        res.stats = stats;

        return res;
    }

    @PostMapping("/statsIdList")
    @ResponseBody
    public List<String> statsIdList() {
        return statsRepository.getStatsIdList();
    }

    @GetMapping("/")
    public String index() {
        return "stats";
    }

    private List<List<Object>> transformValueList(List<Map<String, Object>> ret) {
        return ret.stream().map(map -> map.values().stream().toList()).toList();
    }

    private List<ExecuteStatsColumnDto> transformColumnList(List<Map<String, Object>> ret) {
        if (ret.isEmpty()) {
            return Collections.emptyList();
        }

        return ret.get(0).keySet().stream().map(key -> {
            ExecuteStatsColumnDto dto = new ExecuteStatsColumnDto();
            dto.name = key;
            if (ret.get(0).get(key) instanceof Number) {
                dto.type = "number";
            } else {
                dto.type = "string";
            }
            return dto;
        }).toList();
    }
}
