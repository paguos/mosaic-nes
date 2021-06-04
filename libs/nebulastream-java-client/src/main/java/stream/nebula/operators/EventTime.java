package stream.nebula.operators;
public class EventTime implements TimeCharacteristic{
    private final String field;

    public EventTime(String field) {
        this.field = field;
    }

    @Override
    public String generateCode() {
        return "EventTime(Attribute(\""+field+"\"))";
    }
}
