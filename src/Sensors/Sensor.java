package Sensors;

import Exception.SensorSuspendedExption;
import Exception.ThresholdIsNullException;
import ThresHold.ThresholdRange;
import moduls.Zones;

import java.time.LocalDate;

public class Sensor {
    protected String id;
    protected Zones location;
    protected SensorStatus status;
    protected double value;
    protected LocalDate date;
    protected ThresholdRange thresholdRange;

    // constructor
    public Sensor(String id, Zones location, ThresholdRange thresholdRange, LocalDate date) {
        try {
            if (thresholdRange == null) {
                throw new ThresholdIsNullException("Threshold is null for sensor [" + id + "].");
            }
        } catch (ThresholdIsNullException e) {
            System.out.println(e.getMessage());
        }
        this.id = id;
        this.location = location;
        this.thresholdRange = thresholdRange;
        this.date = date;
        this.status = SensorStatus.ACTIVE;
    }

    // send reading
    public boolean sendReading(double value) {
        this.value = value;
        try {
            requireActiveSensor();
        } catch (SensorSuspendedExption e) {
            System.out.println(e.getMessage());
            return false;
        }
        System.out.println("Sensor " + id + " at " + location.getName() + " sends reading: " + value);
        return true;
    }

    protected void requireActiveSensor() throws SensorSuspendedExption {
        if (status != SensorStatus.ACTIVE) {
            throw new SensorSuspendedExption("Sensor " + id + " is suspended. Cannot send reading.");
        }
    }

    // update status
    public void updateStatus(SensorStatus newStatus) {
        this.status = newStatus;
        System.out.println("Sensor " + id + " status updated to: " + newStatus);
    }

    public void suspend() {
        this.status = SensorStatus.SUSPENDED;
        System.out.println("Sensor [" + id + "] → SUSPENDED");
    }

    public void activate() {
        this.status = SensorStatus.ACTIVE;
        System.out.println("Sensor [" + id + "] → ACTIVE");
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
        System.out.println("  Last Value: " + value + " " + getUnit());
        if (thresholdRange != null) {
            System.out.println(
                    "  Threshold Range: " + thresholdRange.getMinValue() + " to " + thresholdRange.getMaxValue()
                            + " " + getUnit());
        }
    }

    // getters
    public String getId() {
        return id;
    }

    public Zones getLocation() {
        return location;
    }

    public double getValue() {
        return value;
    }

    public LocalDate getDate() {
        return date; // removed stray semicolon
    }

    public String getUnit() {
        return "";
    }
}
