package gradle.task.ast.model;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {
    public String name;
    public String layer;
    public List<String> dependencies = new ArrayList<>();
    public List<MethodInfo> methods = new ArrayList<>();
}
