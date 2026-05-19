package Sensors;
import ThresHold.ThresholdRange;
import Zones.Zone;

import java.time.LocalDate;

public class BiometricSensor extends Sensor {
    protected MeasurementType type;
    // ------------------------------ Constructor ------------------//
    public BiometricSensor(String id, Zone location, ThresholdRange thresholdRange, MeasurementType type, LocalDate date) {
        super(id, location, thresholdRange, date);
        this.type= type;
    }

    @Override
    public void display(){
        displaySensorInfo("Biometric Sensor");
        System.out.println("  Measurement Type: " + type);
    }

    // ------------------------------ Measurement Type Enum ------------------//
    public enum MeasurementType {
        BODY_TEMPERATURE,
        ACTIVITY_LEVEL,
    }


}
