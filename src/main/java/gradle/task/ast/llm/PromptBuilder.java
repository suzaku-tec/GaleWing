package gradle.task.ast.llm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PromptBuilder {

    public void printPrompt(Path projectRoot) throws IOException {
        Path jsonPath = projectRoot.resolve("output/structure.json");
        String json = Files.readString(jsonPath);
        
        System.out.println("""
                あなたはソフトウェアアーキテクトです。
                以下の構造JSONを元に仕様書を作成してください。
                
                制約:
                - 出力はMarkdown
                - UMLはMermaid記法
                - 推測で補完しない
                
                構成:
                1. システム概要
                2. クラス図
                3. シーケンス図
                
                構造JSON:
                """ + json);
    }
}
