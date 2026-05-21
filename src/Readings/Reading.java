package Readings;

import Sensors.Sensor;

public abstract class Reading implements Comparable<Reading> {
    public abstract ReadingStatus evaluate();

    @Override
    public abstract int compareTo(Reading o); // match the generic type

    public abstract void display();

    public abstract Sensor getSensor();

    public abstract double getChartValue();

    public abstract String getUnit();

}
