package Sensors;

import ThresHold.ThresholdRange;
import Zones.Zone;

import java.time.LocalDate;

public class Sensor {
    protected String id;
    protected Zone location;
    protected SensorStatus status;
    protected double value;
    protected LocalDate date;
    protected ThresholdRange thresholdRange;

    // constructor
    public Sensor(String id, Zone location, ThresholdRange thresholdRange, LocalDate date) {
        this.id = id;
        this.location = location;
        this.thresholdRange = thresholdRange;
        this.date = date;
        this.status = SensorStatus.ACTIVE;
    }

    // send reading
    public boolean sendReading(double value) {
        this.value = value;
        if (status == SensorStatus.ACTIVE) {
            System.out.println("Sensor " + id + " at " + location.getName() + " sends reading: " + value);
            return true;
        } else {
            System.out.println("Sensor " + id + " is not active. Cannot send reading.");
            return false;
        }
    }

    // update status
    public void updateStatus(SensorStatus newStatus) {
        this.status = newStatus;
        System.out.println("Sensor " + id + " status updated to: " + newStatus);
    }

    public void display() {
        displaySensorInfo("Sensor");
    }

    protected void displaySensorInfo(String title) {
        System.out.println(title);
        System.out.println("  ID: " + id);
        System.out.println("  Location: " + location.getName());
        System.out.println("  Status: " + status);
        System.out.println("  Date: " + date);
        System.out.println("  Last Value: " + value);
        thresholdRange.displayRange();
    }

    // getters
    public String getId() {
        return id;
    }

    public Zone getLocation() {
        return location;
    }

    public double getValue() {
        return value;
    }

    public LocalDate getDate() {
        return date; // removed stray semicolon
    }
}
