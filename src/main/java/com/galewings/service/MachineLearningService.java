package com.galewings.service;

import com.galewings.dto.ml.FeedMachineLearningDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class MachineLearningService {

    @Autowired
    GwDateService gwDateService;

    public CompletableFuture<String> createArff(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                FeedMachineLearningDto dto = new FeedMachineLearningDto(url);
                outputArff(dto.createInstance());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return "end";
        });
    }

    private void outputArff(Instances instances) throws IOException {
        String timestampStr = GwDateService.DateFormat.FILE_TIME_FORMAT.dtf.format(LocalDateTime.now());

        // ARFFファイルに書き出す
        ArffSaver saver = new ArffSaver();
        saver.setInstances(instances);
        saver.setFile(new File("arff/output_" + timestampStr + ".arff"));
        saver.writeBatch();

    }
}
