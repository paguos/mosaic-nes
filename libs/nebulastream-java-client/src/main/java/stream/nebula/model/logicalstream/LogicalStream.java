package stream.nebula.model.logicalstream;

import java.util.List;

public class LogicalStream {
    private final String name;
    List<Field> fieldList;

    public LogicalStream(String name, List<Field> fieldList) {
        this.name = name;
        this.fieldList = fieldList;
    }

    public String getName() {
        return name;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }
}
