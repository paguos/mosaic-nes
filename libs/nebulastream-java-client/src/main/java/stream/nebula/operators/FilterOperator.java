package stream.nebula.operators;

public class FilterOperator extends Operator{
    private final Predicate predicate;

    public FilterOperator(Predicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public String getCppCode() {
        Predicate currentPredicate = this.predicate;
        StringBuilder cppCode = new StringBuilder(".filter(");
        boolean isLastPredicate = false;

        while(!isLastPredicate) {
            String connector = currentPredicate.getNextPredicateConnector();
            if (connector == null){
                connector = "";
            }
            cppCode.append("Attribute(\"").append(currentPredicate.getField()).append("\")").append(currentPredicate.getOperator()).append(currentPredicate.getValue()).append(connector);
            isLastPredicate = currentPredicate.getNextPredicate()==null;
            currentPredicate = currentPredicate.getNextPredicate();
        }

        cppCode.append(")");

        return cppCode.toString();
    }
}
