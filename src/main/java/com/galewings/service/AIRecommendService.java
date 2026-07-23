package com.galewings.service;

import com.galewings.entity.AIRecommendFeed;
import com.galewings.entity.Feed;
import com.galewings.repository.AIRecommendRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service
public class AIRecommendService {

    private final AIRecommendRepository aiRecommendRepository;

    public AIRecommendService(AIRecommendRepository aiRecommendRepository) {
        this.aiRecommendRepository = aiRecommendRepository;
    }

    public List<AIRecommendFeed> selectAIRecommndFeed() {
        return aiRecommendRepository.selectAIRecommndFeed();
    }

    public List<Feed> selectAIRecommendOrigin(String id) {
        return aiRecommendRepository.selectAIRecommendOrigin(id);
    }

    public void deleteAIRecommend(String id) {
        Map<String, Object> params = Map.of("id", id);
        aiRecommendRepository.deleteAIRecommendOrigin(params);
        aiRecommendRepository.deleteAIRecommendFeed(params);
    }
}
