import Alerts.Alert;
import Alerts.SeverityLevel;
import History.AlertHistory;
import History.ReadingHistory;
import Readings.GPSReading;
import Readings.NumericalReading;
import Sensors.*;
import ThresHold.ThresholdGPS;
import ThresHold.ThresholdRange;
import Zones.Zone;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Create a zone
        Zone zone1 = new Zone("AADE", "Zone 1");
        Zone zone2 = new Zone("AABE", "Zone 2");

        System.out.println("================================");
        System.out.println("Farm Management Demo");
        System.out.println("================================");

        // Create a threshold range for temperature
        System.out.println("Creating sensors and readings...");
        ThresholdRange tempThreshold = new ThresholdRange(0.0, 100.0);
        ThresholdRange humidityThreshold = new ThresholdRange(20, 120);
        ThresholdRange bodyTempThreshold = new ThresholdRange(37, 40);
        ThresholdGPS gpsThreshold = new ThresholdGPS(13, 14, 20);
        ThresholdRange oxThreshold = new ThresholdRange(30, 90);

        // Create a water sensor
        WaterSensor waterSensor1 = new WaterSensor("WS1", zone1, tempThreshold, WaterSensor.MeasurementType.TEMPERATURE,
                LocalDate.of(2026, 6, 7));
        waterSensor1.sendReading(200);
        // create humidity sensor
        EnvironmentalSensor humiditySensor1 = new EnvironmentalSensor("W02", zone1, humidityThreshold,
                EnvironmentalSensor.MeasurementType.HUMIDITY, LocalDate.of(2026, 5, 14));
        humiditySensor1.sendReading(60);

        // create biometric sensors
        BiometricSensor bodyTemp1 = new BiometricSensor("S03", zone1, bodyTempThreshold,
                BiometricSensor.MeasurementType.BODY_TEMPERATURE, LocalDate.of(2025, 5, 9));
        bodyTemp1.sendReading(45);

        // create GPS
        GPSSensor gpsSensor1 = new GPSSensor("G09", zone1, gpsThreshold, LocalDate.of(2026, 5, 19));
        gpsSensor1.sendReading(100, 100);

        GPSSensor gpsSensor2 = new GPSSensor("G07", zone1, gpsThreshold, LocalDate.of(2026, 5, 9));
        gpsSensor2.sendReading(300, 100);

        // all are water sensors
        // Create a water sensor
        WaterSensor waterSensor2 = new WaterSensor("WS2", zone1, tempThreshold, WaterSensor.MeasurementType.TEMPERATURE,
                LocalDate.of(2026, 6, 5));
        waterSensor2.sendReading(340);
        // Create a water sensor
        WaterSensor waterSensor3 = new WaterSensor("WS3", zone1, tempThreshold,
                WaterSensor.MeasurementType.DISSOLVED_OXYGEN, LocalDate.of(2026, 4, 7));
        waterSensor3.sendReading(50);
        // Create a water sensor
        WaterSensor waterSensor4 = new WaterSensor("WS4", zone1, tempThreshold,
                WaterSensor.MeasurementType.DISSOLVED_OXYGEN, LocalDate.of(2025, 3, 7));
        waterSensor4.sendReading(20);

        // Extra inputs to test dashboard and charts
        WaterSensor waterSensor1Day2 = new WaterSensor("WS1", zone1, tempThreshold,
                WaterSensor.MeasurementType.TEMPERATURE, LocalDate.of(2026, 6, 8));
        waterSensor1Day2.sendReading(80);

        WaterSensor waterSensor1Day3 = new WaterSensor("WS1", zone1, tempThreshold,
                WaterSensor.MeasurementType.TEMPERATURE, LocalDate.of(2026, 6, 9));
        waterSensor1Day3.sendReading(130);

        EnvironmentalSensor humiditySensor2 = new EnvironmentalSensor("H02", zone2, humidityThreshold,
                EnvironmentalSensor.MeasurementType.HUMIDITY, LocalDate.of(2026, 5, 20));
        humiditySensor2.sendReading(50);

        EnvironmentalSensor humiditySensor2Day2 = new EnvironmentalSensor("H02", zone2, humidityThreshold,
                EnvironmentalSensor.MeasurementType.HUMIDITY, LocalDate.of(2026, 5, 21));
        humiditySensor2Day2.sendReading(130);

        BiometricSensor bodyTemp2 = new BiometricSensor("B02", zone2, bodyTempThreshold,
                BiometricSensor.MeasurementType.BODY_TEMPERATURE, LocalDate.of(2026, 5, 22));
        bodyTemp2.sendReading(38);

        BiometricSensor bodyTemp2Day2 = new BiometricSensor("B02", zone2, bodyTempThreshold,
                BiometricSensor.MeasurementType.BODY_TEMPERATURE, LocalDate.of(2026, 5, 23));
        bodyTemp2Day2.sendReading(46);

        GPSSensor gpsSensorZone2 = new GPSSensor("G20", zone2, gpsThreshold, LocalDate.of(2026, 5, 24));
        gpsSensorZone2.sendReading(15, 15);

        GPSSensor gpsSensorZone2Day2 = new GPSSensor("G20", zone2, gpsThreshold, LocalDate.of(2026, 5, 25));
        gpsSensorZone2Day2.sendReading(60, 70);

        // Display the threshold range
        // tempThreshold.displayRange();

        // farm manager
        FarmManager f = new FarmManager();

        // reading test
        System.out.println();
        System.out.println("Creating reading objects...");
        NumericalReading r = new NumericalReading(waterSensor1, tempThreshold);
        NumericalReading r02 = new NumericalReading(waterSensor2, tempThreshold);
        NumericalReading r03 = new NumericalReading(waterSensor3, oxThreshold);
        NumericalReading r04 = new NumericalReading(waterSensor4, oxThreshold);
        NumericalReading r05 = new NumericalReading(waterSensor1Day2, tempThreshold);
        NumericalReading r06 = new NumericalReading(waterSensor1Day3, tempThreshold);

        // r.display();

        NumericalReading r1 = new NumericalReading(humiditySensor1, humidityThreshold);
        // r1.display();

        NumericalReading r2 = new NumericalReading(bodyTemp1, bodyTempThreshold);
        // r2.display();

        GPSReading r5 = new GPSReading(gpsSensor1, gpsThreshold);
        // r5.display();

        GPSReading r6 = new GPSReading(gpsSensor2, gpsThreshold);
        // r6.display();

        NumericalReading r7 = new NumericalReading(humiditySensor2, humidityThreshold);
        NumericalReading r8 = new NumericalReading(humiditySensor2Day2, humidityThreshold);
        NumericalReading r9 = new NumericalReading(bodyTemp2, bodyTempThreshold);
        NumericalReading r10 = new NumericalReading(bodyTemp2Day2, bodyTempThreshold);
        GPSReading r11 = new GPSReading(gpsSensorZone2, gpsThreshold);
        GPSReading r12 = new GPSReading(gpsSensorZone2Day2, gpsThreshold);

        // test alerts
        System.out.println();
      //  System.out.println("================================");
       // System.out.println("Alert Tests");
     //   System.out.println("================================");
        Alert alert = new Alert(r2);
       // alert.display();

        showAlertResult("Body temperature alert", alert);

        Alert alert1 = new Alert(r1);
       // showAlertResult("Humidity alert", alert1);

        Alert alert2 = new Alert(r2);
        showAlertResult("Body temperature alert", alert2);

        Alert alert5 = new Alert(r5);
      //  showAlertResult("GPS alert G09", alert5);

        Alert alert6 = new Alert(r6);
      //  showAlertResult("GPS alert G07", alert6);

        AlertHistory alertHistory = new AlertHistory();
        alertHistory.addAlert(alert);
        alertHistory.addAlert(alert1);
        alertHistory.addAlert(alert2);
        alertHistory.addAlert(alert5);
        alertHistory.addAlert(alert6);
        alertHistory.addAlert(new Alert(r));
        alertHistory.addAlert(new Alert(r02));
        alertHistory.addAlert(new Alert(r03));
        alertHistory.addAlert(new Alert(r04));
        alertHistory.addAlert(new Alert(r05));
        alertHistory.addAlert(new Alert(r06));
        alertHistory.addAlert(new Alert(r7));
        alertHistory.addAlert(new Alert(r8));
        alertHistory.addAlert(new Alert(r9));
        alertHistory.addAlert(new Alert(r10));
        alertHistory.addAlert(new Alert(r11));
        alertHistory.addAlert(new Alert(r12));
        alertHistory.displayActivePanelSortedBySeverity();
        alertHistory.alertsHistoryFilterZone(zone2);
        alertHistory.alertsHistoryFilterSensorType(gpsSensorZone2);
        alertHistory.alertsHistoryFilterLevel(SeverityLevel.CRITICAL);
        alertHistory.alertsHistoryFilterTimePeriod(
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 31));

        //test filter
        alertHistory.browseAlerts(
                zone2,
                gpsSensorZone2,
                SeverityLevel.CRITICAL,
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 31));

        // test reading history
        ReadingHistory waterSensorHistory = new ReadingHistory();
        waterSensorHistory.getReadingsHistory().put(r.getSensor().getDate(), r);
        waterSensorHistory.getReadingsHistory().put(r02.getSensor().getDate(), r02);
        waterSensorHistory.getReadingsHistory().put(r03.getSensor().getDate(), r03);
        waterSensorHistory.getReadingsHistory().put(r04.getSensor().getDate(), r04);
        waterSensorHistory.getReadingsHistory().put(r05.getSensor().getDate(), r05);
        waterSensorHistory.getReadingsHistory().put(r06.getSensor().getDate(), r06);
        waterSensorHistory.getReadingsHistory().put(r1.getSensor().getDate(), r1);
        waterSensorHistory.getReadingsHistory().put(r2.getSensor().getDate(), r2);
        waterSensorHistory.getReadingsHistory().put(r5.getSensor().getDate(), r5);
        waterSensorHistory.getReadingsHistory().put(r6.getSensor().getDate(), r6);
        waterSensorHistory.getReadingsHistory().put(r7.getSensor().getDate(), r7);
        waterSensorHistory.getReadingsHistory().put(r8.getSensor().getDate(), r8);
        waterSensorHistory.getReadingsHistory().put(r9.getSensor().getDate(), r9);
        waterSensorHistory.getReadingsHistory().put(r10.getSensor().getDate(), r10);
        waterSensorHistory.getReadingsHistory().put(r11.getSensor().getDate(), r11);
        waterSensorHistory.getReadingsHistory().put(r12.getSensor().getDate(), r12);

       // waterSensorHistory.display();
        waterSensorHistory.displayDashboardByZone();
        waterSensorHistory.displayEvolutionChartBySensor();
        waterSensorHistory.displayEvolutionChartByZone();

        waterSensorHistory.filterRange(
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 6, 30));

    }

    private static void showAlertResult(String title, Alert alert) {
        System.out.println("--------------------------------");
        System.out.println(title + ": " + (alert.lunchAlert() ? "LAUNCHED" : "NOT LAUNCHED"));
    }
}
