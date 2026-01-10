package gradle.task.ast.model;

import java.util.ArrayList;
import java.util.List;

public class MethodInfo {
    public String name;
    public String returnType;
    public List<String> parameters = new ArrayList<>();
}
