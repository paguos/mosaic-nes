package stream.nebula.operators;

public class TimeMeasure {

    String measurement;
    int value;

    private TimeMeasure(String measurement, int value) {
        this.measurement = measurement;
        this.value = value;
    }

    public static TimeMeasure milliseconds(int value){
        return new TimeMeasure("Milliseconds", value);
    }

    public static TimeMeasure seconds(int value){
        return new TimeMeasure("Seconds", value);
    }

    public static TimeMeasure minutes(int value){
        return new TimeMeasure("Minutes", value);
    }

    public static TimeMeasure hours(int value){
        return new TimeMeasure("Hours", value);
    }


    public String getMeasurement() {
        return measurement;
    }

    public int getValue() {
        return value;
    }
}
