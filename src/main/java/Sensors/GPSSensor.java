package Sensors;

import ThresHold.ThresholdGPS;
import Zones.Zone;

import java.time.LocalDate;

public class GPSSensor extends Sensor {
    protected double latitude;
    protected double longitude;
    protected ThresholdGPS range;

    // constructor
    public GPSSensor(String id, Zone location, ThresholdGPS range, LocalDate date) {
        super(id, location, range, date); // works if ThresholdGPS extends ThresholdRange
    }

    // send reading
    public boolean sendReading(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        if (status == SensorStatus.ACTIVE) {
            System.out.println("GPS Sensor " + id + " at " + location.getName()
                + " sends reading: Latitude: " + latitude + ", Longitude: " + longitude);
            return true;
        } else {
            System.out.println("GPS Sensor " + id + " is inactive. Cannot send reading.");
            return false;
        }

    }

    @Override
    public void display() {
        System.out.println("GPS Sensor");
        System.out.println("  ID: " + id);
        System.out.println("  Location: " + location.getName());
        System.out.println("  Status: " + status);
        System.out.println("  Date: " + date);
        System.out.println("  Current Latitude: " + latitude + " decimal degrees");
        System.out.println("  Current Longitude: " + longitude + " decimal degrees");
        thresholdRange.displayRange();
    }

    // getters
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String getUnit() {
        return "km";
    }
}
