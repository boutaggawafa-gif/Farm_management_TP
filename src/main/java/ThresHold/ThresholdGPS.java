package ThresHold;

public class ThresholdGPS extends ThresholdRange {
    protected double centerLongitude;
    protected double centerLatitude;
    protected   double radius;

    public ThresholdGPS(double centerLatitude, double centerLongitude, double radius) {
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.radius = radius;
    }

    @Override
    public void displayRange(){
        System.out.println("  GPS Threshold:");
        System.out.println("    Center Latitude: " + centerLatitude);
        System.out.println("    Center Longitude: " + centerLongitude);
        System.out.println("    Radius: " + radius + " km");
        System.out.println("    Perimeter: " + 2 * Math.PI * radius + " km");
    }


    // getters


    public double getCenterLatitude() {
        return centerLatitude;
    }

    public double getCenterLongitude() {
        return centerLongitude;
    }

    public double getRadius() {
        return radius;
    }
}
