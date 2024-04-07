package com.galewings.task;

import com.galewings.dto.ai.googleai.response.GeminiResponseDto;
import com.galewings.entity.Settings;
import com.galewings.repository.NewsSummaryRepository;
import com.galewings.repository.SettingRepository;
import com.galewings.service.GeminiService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AutoSummaryTask {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private NewsSummaryRepository newsSummaryRepository;

    @Autowired
    private SettingRepository settingRepository;

    private static final String SUMMARY_REQUEST_ID = "summary_request";

    /**
     * 待ち時間
     */
    private final long WAITE_TIME = 20000;

    @Scheduled(cron = "${update.summary.scheduler.cron}")
    public void autoSammary() {
        Settings summaryRequest = settingRepository.selectOneFor(SUMMARY_REQUEST_ID);

        if (Objects.isNull(summaryRequest) || Objects.isNull(summaryRequest.setting)) {
            return;
        }

        newsSummaryRepository.selectNoSummary().stream().map(news -> {
            String text = summaryRequest.setting + "\r\n" + news.feedUuid;
            String summary = null;
            try {
                var response = geminiService.tellMe(text);

                summary = getSummaryText(response);
                Thread.sleep(WAITE_TIME);
            } catch (Exception e) {
                summary = "";
            }
            news.setSummary(summary);
            return news;
        }).forEach(newsSummary -> {
            newsSummaryRepository.update(newsSummary.getFeedUuid(), newsSummary.getSummary());
        });
    }

    private String getSummaryText(GeminiResponseDto responseDto) {
        if (Objects.isNull(responseDto.getCandidates())) {
            return getNonResponseAltText(responseDto);
        } else {
            return responseDto.getCandidates().get(0).getContent().getParts().get(0).getText();
        }
    }

    private String getNonResponseAltText(GeminiResponseDto responseDto) {
        return responseDto.getPromptFeedback().getSafetyRatings().stream().map(safetyRatingsDto -> safetyRatingsDto.getCategory())
                .map(category -> GeminiResCategory.getEnum(category))
                .map(geminiResCategory -> geminiResCategory.ALTERNATIVE_STRING)
                .filter(str -> !Strings.isNullOrEmpty(str))
                .collect(Collectors.joining("\r\n"));
    }

    private enum GeminiResCategory {

        HARM_CATEGORY_SEXUALLY_EXPLICIT("えっちなのはいけないと思います", "HARM_CATEGORY_SEXUALLY_EXPLICIT"),
        HARM_CATEGORY_HATE_SPEECH("あまり強い言葉を使うなよ", "HARM_CATEGORY_HATE_SPEECH"),
        HARM_CATEGORY_HARASSMENT("えっちなのはいけないと思います", "HARM_CATEGORY_HARASSMENT"),
        HARM_CATEGORY_DANGEROUS_CONTENT("危険が危ない", "HARM_CATEGORY_DANGEROUS_CONTENT"),
        NONE(null, "NONE"),
        ;
        public String ALTERNATIVE_STRING;
        public String ID;

        GeminiResCategory(String alternativeString, String id) {
            this.ALTERNATIVE_STRING = alternativeString;
            this.ID = id;
        }

        public static GeminiResCategory getEnum(String id) {
            var res = Arrays.stream(values()).filter(geminiResCategory -> geminiResCategory.ID.equals(id)).findFirst();
            return res.orElse(NONE);
        }
    }
}
