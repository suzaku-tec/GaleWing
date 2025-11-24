package com.galewings.task.bat;

import com.galewings.service.CustomRssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(transactionManager = "customRssTransactionManager")
public class CustomeRssExtractTask implements Runnable {

    private final CustomRssService customRssService;

    @Autowired
    public CustomeRssExtractTask(CustomRssService customRssService) {
        this.customRssService = customRssService;
    }

    @Override
    public void run() {
        customRssService.extractDiff();
    }
}
