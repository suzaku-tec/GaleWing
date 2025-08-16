package com.galewings.service;

import com.galewings.dto.relation.FeedRelation;
import com.galewings.repository.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationService {
    @Autowired
    private RelationRepository relationRepository;

    public List<FeedRelation> list(String uuid) {
        return relationRepository.selectFeedRelationList(uuid);
    }
}
