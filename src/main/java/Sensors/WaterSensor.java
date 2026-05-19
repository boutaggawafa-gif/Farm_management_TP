package Sensors;

import ThresHold.ThresholdRange;
import Zones.Zone;

import java.time.LocalDate;

public class WaterSensor extends Sensor {
    protected MeasurementType type;

    public WaterSensor(String id, Zone location, ThresholdRange thresholdRange, MeasurementType type, LocalDate date) {
        super(id, location, thresholdRange, date);
        this.type = type;
    }


    @Override
    public void display(){
        displaySensorInfo("Water Sensor");
        System.out.println("  Measurement Type: " + type);
    }

    @Override
    public String getUnit() {
        if (type == MeasurementType.TEMPERATURE) {
            return "C";
        }
        if (type == MeasurementType.DISSOLVED_OXYGEN) {
            return "mg/L";
        }
        return "";
    }


    public enum MeasurementType {
        TEMPERATURE,
        DISSOLVED_OXYGEN,
    }

}

