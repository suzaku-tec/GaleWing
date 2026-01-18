package com.galewings.service;

import com.galewings.dto.statistics.ReadRateDto;
import com.galewings.dto.statistics.ranking.RankDto;
import com.galewings.dto.statistics.ranking.RankingDto;
import com.galewings.dto.statistics.ranking.RankingdDatasetDto;
import com.galewings.repository.StaticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class StaticsService {

    private final String[] colors = {"#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0", "#9966FF", "#FF9F40"};

    private final StaticsRepository staticsRepository;

    private final GwDateService gwDateService;

    @Autowired
    public StaticsService(StaticsRepository staticsRepository, GwDateService gwDateService) {
        this.staticsRepository = staticsRepository;
        this.gwDateService = gwDateService;
    }

    public ReadRateDto selectReadRate() {
        return staticsRepository.selectReadRate();
    }

    public RankingDto selectWordRank() {
        RankingDto rankingDto = new RankingDto();

        String maxDate = gwDateService.now().format(GwDateService.DateFormat.SQLITE_DATE_FORMAT.dtf);
        String minDate = gwDateService.now().minusDays(7).format(GwDateService.DateFormat.SQLITE_DATE_FORMAT.dtf);
        List<RankDto> rankList = staticsRepository.selectWordRank(minDate, maxDate);

        rankingDto.labels = rankList.stream().map(rank -> rank.label).distinct().toList();

        Map<String, List<Integer>> result = rankList.stream()
                .collect(Collectors.groupingBy(rankDto -> rankDto.data,
                        Collectors.mapping(rankDto -> rankDto.rank, Collectors.toList())));

        AtomicInteger index = new AtomicInteger(0); // カウンタを用意
        rankingDto.datasets = result.keySet().stream().map(key -> {


            RankingdDatasetDto dto = new RankingdDatasetDto();
            dto.label = key;
            dto.data = result.get(key).stream().map(value -> 10 < value ? null : value).toList();
            dto.borderColor = colors[index.getAndIncrement() % colors.length];
            dto.backgroundColor = dto.borderColor;
            return dto;
        }).toList();

        return rankingDto;
    }
}
