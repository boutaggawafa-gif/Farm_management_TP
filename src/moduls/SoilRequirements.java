package moduls;

public class SoilRequirements {
    private double minPH ;
    private  double maxPH;
    private double minMoisture ;
    private double maxMoisture ;

    public SoilRequirements(double minPH, double maxPH, double minMoisture, double maxMoisture) {
        this.minPH = minPH;
        this.maxPH = maxPH;
        this.minMoisture = minMoisture;
        this.maxMoisture = maxMoisture;
    }

    public void display() {
        System.out.println("  Soil Requirements:");
        System.out.println("    pH      : " + minPH + " → " + maxPH);
        System.out.println("    Moisture: " + minMoisture + " → " + maxMoisture);
    }
}
