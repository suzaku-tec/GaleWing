package com.galewings.dto.output;

import com.galewings.entity.Stats;

import java.util.List;

public class ExecuteStatsResultDto {
    public List<ExecuteStatsColumnDto> columns;
    public List<List<Object>> values;
    public Stats stats;
}
