package com.galewings.task.bat;

import com.galewings.task.AutoSummaryTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SampleGemini implements Runnable {

    @Autowired
    private AutoSummaryTask task;

    @Override
    public void run() {
        task.autoSammary();
    }
}
