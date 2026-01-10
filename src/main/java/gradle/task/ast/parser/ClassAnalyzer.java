package gradle.task.ast.parser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import gradle.task.ast.model.ClassInfo;
import gradle.task.ast.model.MethodInfo;

public class ClassAnalyzer {

    public static ClassInfo analyze(ClassOrInterfaceDeclaration c) {
        ClassInfo info = new ClassInfo();
        info.name = c.getNameAsString();
        info.layer = detectLayer(c);

        c.getFields().forEach(f ->
                f.getVariables().forEach(v ->
                        info.dependencies.add(v.getType().asString())
                )
        );

        for (MethodDeclaration m : c.getMethods()) {
            MethodInfo mi = new MethodInfo();
            mi.name = m.getNameAsString();
            mi.returnType = m.getType().asString();
            m.getParameters().forEach(p ->
                    mi.parameters.add(p.getType().asString())
            );
            info.methods.add(mi);
        }

        return info;
    }

    private static String detectLayer(ClassOrInterfaceDeclaration c) {
        if (c.isAnnotationPresent("Controller") || c.getNameAsString().endsWith("Controller"))
            return "Controller";
        if (c.isAnnotationPresent("Service") || c.getNameAsString().endsWith("Service"))
            return "Service";
        if (c.isAnnotationPresent("Repository") || c.getNameAsString().endsWith("Repository"))
            return "Repository";
        if (c.isAnnotationPresent("Entity"))
            return "Entity";
        return "Other";
    }
}
