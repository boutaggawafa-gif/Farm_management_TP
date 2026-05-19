package Readings;

import Sensors.GPSSensor;
import ThresHold.ThresholdGPS;

public class GPSReading extends Reading {
    GPSSensor sensor;
    ThresholdGPS range;
    ReadingStatus status;

    // constructor
    public GPSReading(GPSSensor sensor, ThresholdGPS range) {
        this.sensor = sensor;
        this.range = range;
        this.status = evaluate();

    }

    public ReadingStatus evaluate() {
        double latDiff = sensor.getLatitude() - range.getCenterLatitude();
        double lonDiff = sensor.getLongitude() - range.getCenterLongitude();
        double distance = Math.sqrt((latDiff * latDiff) + (lonDiff * lonDiff));

        if (distance <= range.getRadius()) {
            status = ReadingStatus.NORMAL;
            return status;
        }
        status = ReadingStatus.CRITICAL; // animal is out of bounds
        return status;
    }

    public void display() {
        System.out.println("GPS Reading");
        sensor.display();
        System.out.println("  Reading Status: " + status);
    }

    public ReadingStatus getStatus() {
        return status;
    }

    public GPSSensor getSensor() {
        return sensor;
    }

    @Override
    public double getChartValue() {
        double latDiff = sensor.getLatitude() - range.getCenterLatitude();
        double lonDiff = sensor.getLongitude() - range.getCenterLongitude();
        return Math.sqrt((latDiff * latDiff) + (lonDiff * lonDiff));
    }

    @Override
    public String getUnit() {
        return "km from center";
    }

    @Override
    public int compareTo(Reading o) { // Reading, not Object
        GPSReading other = (GPSReading) o; // cast to access sensor
        return this.sensor.getDate().compareTo(other.sensor.getDate()); // use compareTo for dates
    }

}
