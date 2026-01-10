package gradle.task;

import gradle.task.ast.llm.PromptBuilder;
import gradle.task.ast.model.ClassInfo;
import gradle.task.ast.model.UseCaseInfo;
import gradle.task.ast.output.JsonExporter;
import gradle.task.ast.parser.SequenceAnalyzer;
import gradle.task.ast.parser.SourceScanner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 「AST解析 → 構造JSON → LLM → Markdown仕様書（Mermaid UML含む）」 を
 * 実務で実装可能なレベル まで具体化して提案します。
 * <p>
 * 前提として、ここでは Java + Spring Boot を主軸に説明しますが、TypeScript 等でもほぼ同型で置き換え可能です
 * <p>
 * [ ソースコード ]
 * ↓
 * [ AST解析 ]
 * ↓
 * [ 構造JSON生成 ]
 * ↓
 * [ LLM入力整形 ]
 * ↓
 * [ Markdown仕様書 + Mermaid UML ]
 */
public class ASTExtractor {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            throw new IllegalArgumentException("解析対象プロジェクトのルートパスを指定してください");
        }

        Path projectRoot = Paths.get(args[0]);
        Path sourceRoot = projectRoot.resolve("src/main/java");

        System.out.println("AST解析開始");
        System.out.println("Project Root : " + projectRoot);
        System.out.println("Source Root  : " + sourceRoot);

        SourceScanner scanner = new SourceScanner(sourceRoot);
        List<ClassInfo> classes = scanner.scan();

        SequenceAnalyzer sequenceAnalyzer = new SequenceAnalyzer();
        List<UseCaseInfo> useCases = sequenceAnalyzer.analyze(classes);

        JsonExporter exporter = new JsonExporter();
        exporter.export(projectRoot, classes, useCases);

        PromptBuilder promptBuilder = new PromptBuilder();
        promptBuilder.printPrompt(projectRoot);

        System.out.println("AST解析完了");
    }

}