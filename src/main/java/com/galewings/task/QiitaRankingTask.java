package com.galewings.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.galewings.dto.qiita.QiitaItemsDto;
import com.galewings.repository.QiitaRankingRepository;
import com.galewings.repository.QiitaTagRepository;
import com.galewings.repository.SettingRepository;
import com.galewings.service.URLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Component
public class QiitaRankingTask {

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private QiitaRankingRepository qiitaRankingRepository;

    @Autowired
    private QiitaTagRepository qiitaTagRepository;

    @Autowired
    private URLService urlService;

    public void ranking() throws URISyntaxException, IOException, InterruptedException {

        String accessToken = settingRepository.selectOneFor("qiita_access_token").setting;

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        String weekStartDate = getWeekStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE);

        // テスト用のパラメータを作成
        Map<String, String> params = Map.of(
                "page", "1",
                "per_page", "10",
                "query", String.join("+", "created:>" + weekStartDate, "stocks:>10")
        );

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(urlService.buildQueryStringURI("https://qiita.com/api/v2/items", params))
                .header("Authorization", "Bearer " + accessToken)
                .header("accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        final CollectionType jt
                = mapper.getTypeFactory().constructCollectionType(List.class, QiitaItemsDto.class);
        List<QiitaItemsDto> val = mapper.readValue(response.body(), jt);

        qiitaRankingRepository.allDelete();
        IntStream.range(0, val.size() - 1).mapToObj(rank -> {
            QiitaItemsDto dto = val.get(rank);
            dto.ranking = rank + 1;
            return dto;
        }).forEach(qiitaRankingRepository::insert);

        val.forEach(item -> {
            item.tags.stream().filter(tag -> !qiitaTagRepository.isExist(item.url, tag.name))
                    .forEach(tag -> qiitaTagRepository.insert(item.url, tag.name));
        });
    }

    private LocalDate getWeekStartDate() {
        // 現在の日付を取得
        LocalDate now = LocalDate.now();

        // 曜日を取得
        return now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }
}
