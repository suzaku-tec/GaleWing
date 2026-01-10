package gradle.task.ast.parser;

import gradle.task.ast.model.ClassInfo;
import gradle.task.ast.model.UseCaseInfo;

import java.util.ArrayList;
import java.util.List;

public class SequenceAnalyzer {

    public List<UseCaseInfo> analyze(List<ClassInfo> classes) {
        List<UseCaseInfo> result = new ArrayList<>();

        classes.stream()
                .filter(c -> "Controller".equals(c.layer))
                .forEach(controller -> {
                    controller.methods.forEach(m -> {
                        UseCaseInfo uc = new UseCaseInfo();
                        uc.name = controller.name + "." + m.name;
                        uc.sequence.add(controller.name);

                        classes.stream()
                                .filter(c -> "Service".equals(c.layer))
                                .forEach(s -> uc.sequence.add(s.name));

                        result.add(uc);
                    });
                });

        return result;
    }
}
