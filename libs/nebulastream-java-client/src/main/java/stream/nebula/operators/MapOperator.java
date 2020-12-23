package stream.nebula.operators;

public class MapOperator extends Operator{
    private final Map mapFuntion;

    public MapOperator(Map mapFunction) {
        this.mapFuntion = mapFunction;
    }

    @Override
    public String getCppCode() {
        StringBuilder cppCode = new StringBuilder(".map(");

        cppCode.append("Attribute(\"").append(mapFuntion.getFieldName()).append("\") = ");

        // Attribute(x) = Attribute(y) + Attribute(z)
        switch (mapFuntion.getOperation().getOperator()) {
            case "add":
                cppCode.append("Attribute(\"").append(mapFuntion.getOperation().getLeftFieldName()).append("\") + Attribute(\"").append(mapFuntion.getOperation().getRightFieldName()).append("\")");
                break;
            case "substract":
                cppCode.append("Attribute(\"").append(mapFuntion.getOperation().getLeftFieldName()).append("\") - Attribute(\"").append(mapFuntion.getOperation().getRightFieldName()).append("\")");
                break;
            case "multiply":
                cppCode.append("Attribute(\"").append(mapFuntion.getOperation().getLeftFieldName()).append("\") * Attribute(\"").append(mapFuntion.getOperation().getRightFieldName()).append("\")");
                break;
            case "divide":
                cppCode.append("Attribute(\"").append(mapFuntion.getOperation().getLeftFieldName()).append("\") / Attribute(\"").append(mapFuntion.getOperation().getRightFieldName()).append("\")");
                break;
            case "constant":
                cppCode.append(mapFuntion.getOperation().getConstantNumber());
                break;
        }

        cppCode.append(")");
        return cppCode.toString();
    }
}
