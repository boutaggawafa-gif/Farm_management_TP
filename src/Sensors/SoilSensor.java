package Sensors;


import ThresHold.ThresholdRange;
import moduls.Zones;

import java.time.LocalDate;

public class SoilSensor extends Sensor {
    protected MeasurementType type;

    public SoilSensor(String id, Zones location, ThresholdRange thresholdRange, MeasurementType type, LocalDate  date) {
        super(id, location, thresholdRange, date);
        this.type= type;
    }

    @Override
    public void display(){
        displaySensorInfo("Soil Sensor");
        System.out.println("  Measurement Type: " + type);
    }

    @Override
    public String getUnit() {
        if (type == MeasurementType.PH) {
            return "pH";
        }
        if (type == MeasurementType.MOISTURE) {
            return "%";
        }
        if (type == MeasurementType.NITROGIN_CONTENT) {
            return "mg/kg";
        }
        return "";
    }


    public enum MeasurementType {
        PH,
        MOISTURE,
        NITROGIN_CONTENT,
    }

}

