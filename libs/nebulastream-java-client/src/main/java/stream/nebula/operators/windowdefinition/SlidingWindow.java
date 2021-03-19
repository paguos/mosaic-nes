package stream.nebula.operators.windowdefinition;

import stream.nebula.operators.TimeCharacteristic;
import stream.nebula.operators.TimeMeasure;

public class SlidingWindow extends WindowDefinition {
    TimeCharacteristic timeCharacteristic;
    TimeMeasure size;
    TimeMeasure slide;

    private SlidingWindow(TimeCharacteristic timeCharacteristic, TimeMeasure size, TimeMeasure slide) {
        this.timeCharacteristic = timeCharacteristic;
        this.size = size;
        this.slide = slide;
    }

    public static SlidingWindow of(TimeCharacteristic timeCharacteristic, TimeMeasure size, TimeMeasure slide) {
        return new SlidingWindow(timeCharacteristic, size, slide);
    }

    @Override
    public String toString() {
        return "SlidingWindow::of("+timeCharacteristic.generateCode()+", "+size.getMeasurement()+"("+size.getValue()+"), "+slide.getMeasurement()+"("+slide.getValue()+"))";
    }
}
