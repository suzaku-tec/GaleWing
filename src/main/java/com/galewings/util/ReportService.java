package com.galewings.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class ReportService {

    @Value("${report.output.dir}")
    private String outputDir;


    public void report(String fileName, String content) throws IOException {
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();  // ディレクトリがなければ作成
        }

        File outputFile = new File(dir, fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(content);
            writer.flush();
        }
    }
}
