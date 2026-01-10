package gradle.task.ast.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import gradle.task.ast.model.ClassInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SourceScanner {

    private final Path root;
    private final JavaParser parser = new JavaParser();

    public SourceScanner(Path root) {
        this.root = root;
    }

    public List<ClassInfo> scan() throws IOException {
        List<ClassInfo> result = new ArrayList<>();

        Files.walk(root)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        CompilationUnit cu = parser.parse(path).getResult().orElseThrow();
                        cu.findAll(ClassOrInterfaceDeclaration.class)
                                .forEach(c -> result.add(ClassAnalyzer.analyze(c)));
                    } catch (Exception ignored) {
                    }
                });

        return result;
    }
}