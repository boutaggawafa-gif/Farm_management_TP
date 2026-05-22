package Alerts;

import Readings.Reading;
import Readings.ReadingStatus;

public class Alert {
    Reading reading;
    SeverityLevel level;
    AlertStatus status;

    // constructor
    public Alert(Reading reading) {
        this.reading = reading;
        this.level = evaluate(reading);
    }

    public SeverityLevel evaluate(Reading reading) {
        ReadingStatus status = reading.evaluate(); // get the status from the reading

        if (status.equals(ReadingStatus.NORMAL)) {
            level = SeverityLevel.NORMAL;
        } else if (status.equals(ReadingStatus.WARNING)) {
            level = SeverityLevel.WARNING;
        } else if (status.equals(ReadingStatus.CRITICAL)) {
            level = SeverityLevel.CRITICAL;
        }
        return level;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }

    public boolean lunchAlert() {
        this.evaluate(reading);
        if (level.equals(SeverityLevel.WARNING) || level.equals(SeverityLevel.CRITICAL)) {
            return true;
        }
        return false;
    }

    public void display() {
        System.out.println("================================");
        System.out.println("Alert");
        System.out.println("--------------------------------");
        reading.display();
        System.out.println("  Alert Level: " + level);
        System.out.println("================================");
    }


    public void acknowledge(){
        display();
    }

    public void dismiss(){
        System.out.println("Alert dismissed!");
    }

    public Reading getReading() {
        return reading;
    }

    public SeverityLevel getLevel() {
        return level;
    }

}
