package gradle.task.ast.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gradle.task.ast.model.ClassInfo;
import gradle.task.ast.model.UseCaseInfo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JsonExporter {

    public void export(Path projectRoot, List<ClassInfo> classes, List<UseCaseInfo> useCases) throws Exception {
        Path outputDir = projectRoot.resolve("output");
        Files.createDirectories(outputDir);

        Path outputFile = outputDir.resolve("structure.json");

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        root.putPOJO("classes", classes);
        root.putPOJO("useCases", useCases);

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(outputFile.toFile(), root);
    }
}
