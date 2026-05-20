package Sensors;

import ThresHold.ThresholdRange;
import Zones.Zone;

import java.time.LocalDate;

public class EnvironmentalSensor extends Sensor {
    protected MeasurementType type;

    // ----------------------------- Constructor ------------------//
    public EnvironmentalSensor(String id, Zone location, ThresholdRange thresholdRange, MeasurementType type, LocalDate date) {
        super(id, location, thresholdRange , date);
        this.type = type;
    }


    @Override
    public void display(){
        displaySensorInfo("Environmental Sensor");
        System.out.println("  Measurement Type: " + type);
    }

    @Override
    public String getUnit() {
        if (type == MeasurementType.TEMPERATURE) {
            return "C";
        }
        if (type == MeasurementType.HUMIDITY) {
            return "%";
        }
        if (type == MeasurementType.RAINFALL) {
            return "mm";
        }
        return "";
    }

    // ----------------------------- Sensor Type Enum ------------------//
    public enum MeasurementType {
        TEMPERATURE,
        HUMIDITY,
        RAINFALL,
    }
}

