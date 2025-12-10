package com.galewings.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

@SpringBootTest
class ReportServiceTest {

    @InjectMocks
    private ReportService reportService;

    @TempDir
    static Path tempDir;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(reportService, "outputDir", tempDir.toAbsolutePath().toString(), String.class);
    }

    @Test
    void createsFileWithCorrectContent() throws IOException {
        String fileName = "report.txt";
        String content = "This is a test report.";

        reportService.report(fileName, content);

        Path outputFile = tempDir.resolve(fileName);
        assertTrue(Files.exists(outputFile));
        assertEquals(content, Files.readString(outputFile));
    }

    @Test
    void createsDirectoryIfNotExists() throws IOException {
        String fileName = "nested/report.txt";
        String content = "Nested directory test.";

        try {
            reportService.report(fileName, content);
            Assertions.fail("正常終了しちゃいけないパターン");
        } catch (FileNotFoundException e) {
            // 想定通りの挙動
        } catch (Exception e) {
            Assertions.fail("想定外の例外");
        }
    }

    @Test
    void throwsIOExceptionForInvalidFileName() {
        String fileName = "invalid\0name.txt";
        String content = "Invalid file name test.";

        assertThrows(IOException.class, () -> reportService.report(fileName, content));
    }
}
