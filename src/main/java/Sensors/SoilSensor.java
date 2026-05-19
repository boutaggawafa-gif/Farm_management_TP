package Sensors;


import ThresHold.ThresholdRange;
import Zones.Zone;

import java.time.LocalDate;

public class SoilSensor extends Sensor {
    protected MeasurementType type;

    public SoilSensor(String id, Zone location, ThresholdRange thresholdRange, MeasurementType type, LocalDate  date) {
        super(id, location, thresholdRange, date);
        this.type= type;
    }

    @Override
    public void display(){
        displaySensorInfo("Soil Sensor");
        System.out.println("  Measurement Type: " + type);
    }


    public enum MeasurementType {
        PH,
        MOISTURE,
        NITROGIN_CONTENT,
    }

}

