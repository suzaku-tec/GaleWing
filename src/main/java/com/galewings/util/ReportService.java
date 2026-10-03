package com.galewings.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Component
public class ReportService {

    @Value("${report.output.dir}")
    private String outputDir;


    public void report(String fileName, String content) throws IOException {
        File dir = new File(outputDir);
        if (!dir.exists()) {
            boolean result = dir.mkdirs();  // ディレクトリがなければ作成

            if (!result) {
                throw new IOException("Unable to create directory");
            }
        }

        File outputFile = new File(dir, fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(content);
            writer.flush();
        }
    }

    public List<String> getReportList() throws IOException {

        try (Stream<Path> stream = Files.list(Paths.get(outputDir))) {
            return stream.filter(path -> {
                        try {
                            return !Files.isHidden(path);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .map(path -> path.getFileName().toString())
                    .toList();

        }
    }
}
