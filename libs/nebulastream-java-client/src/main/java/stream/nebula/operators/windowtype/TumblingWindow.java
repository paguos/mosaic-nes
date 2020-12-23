package stream.nebula.operators.windowtype;

import stream.nebula.operators.TimeCharacteristic;
import stream.nebula.operators.TimeMeasure;

public class TumblingWindow extends WindowType {
    TimeCharacteristic timeCharacteristic;
    TimeMeasure size;
    private TumblingWindow(TimeCharacteristic timeCharacteristic, TimeMeasure size){
        this.timeCharacteristic = timeCharacteristic;
        this.size = size;
    }

    public static TumblingWindow of(TimeCharacteristic timeCharacteristic, TimeMeasure timeMeasure) {
        return new TumblingWindow(timeCharacteristic, timeMeasure);
    }


    @Override
    public String toString() {
        return "TumblingWindow::of("+timeCharacteristic.generateCode()+", "+size.getMeasurement()+"("+ size.getValue()+"))";
    }
}
