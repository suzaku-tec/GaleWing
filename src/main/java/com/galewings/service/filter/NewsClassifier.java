package com.galewings.service.filter;

import com.galewings.entity.Feed;

public interface NewsClassifier {

    ClassificationResult classify(Feed feed);
}

