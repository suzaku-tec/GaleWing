package com.galewings.service;

import com.galewings.dto.statistics.ReadRateDto;
import com.galewings.repository.StaticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaticsService {

    private final StaticsRepository staticsRepository;

    @Autowired
    public StaticsService(StaticsRepository staticsRepository) {
        this.staticsRepository = staticsRepository;
    }

    public ReadRateDto selectReadRate() {
        return staticsRepository.selectReadRate();
    }
}
