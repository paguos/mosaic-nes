package stream.nebula.operators;

import stream.nebula.model.logicalstream.Field;

public class EventTime implements TimeCharacteristic{
    private String field;

    public EventTime(String field) {
        this.field = field;
    }

    @Override
    public String generateCode() {
        return "EventTime(Attribute(\""+field+"\"))";
    }
}
