package Readings;

import Sensors.Sensor;
import ThresHold.ThresholdRange;

public class NumericalReading extends Reading {
    Sensor sensor;
    ThresholdRange range;
    Readings.ReadingStatus status;

    // constructor
    public NumericalReading(Sensor sensor, ThresholdRange range) {
        this.sensor = sensor;
        this.range = range;
        this.status = evaluate();
    }

    public Readings.ReadingStatus evaluate() {
        double value = sensor.getValue();
        if (value > range.getMinValue() && value < range.getMaxValue()) {
            status = Readings.ReadingStatus.NORMAL;
            return status;
        } else if ((value > range.getMaxValue() && value < (range.getMaxValue() * 1.5))
            || (value < range.getMinValue() && value > (range.getMinValue() * 0.5))) {
            status = ReadingStatus.WARNING;
            return status;
        } else {
            status = ReadingStatus.CRITICAL;
            return status;
        }
    }

    public void display(){
        System.out.println("Numerical Reading");
        sensor.display();
        System.out.println("  Reading Status: " + status);
    }

    // getters
    public Sensor getSensor() {
        return this.sensor;
    }
    public ReadingStatus getStatus() { return this.status;};

    @Override
    public double getChartValue() {
        return sensor.getValue();
    }

    @Override
    public String getUnit() {
        return sensor.getUnit();
    }

    @Override
    public int compareTo(Reading o) {              // Reading, not Object
        NumericalReading other = (NumericalReading) o; // cast to access sensor
        return this.sensor.getDate().compareTo(other.sensor.getDate()); // use compareTo for dates
    }



}
