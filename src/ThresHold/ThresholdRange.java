package ThresHold;

public class ThresholdRange {

    protected double minValue;
    protected double maxValue;

    public ThresholdRange(double minValue, double maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public ThresholdRange(){};

    public void displayRange() {
        System.out.println("  Threshold Range: " + minValue + " to " + maxValue);
    }


    // getters & setters


    public double getMaxValue() {
        return maxValue;
    }

    public double getMinValue() {
        return minValue;
    }
}
