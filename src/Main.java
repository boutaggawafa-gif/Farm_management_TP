import moduls.*;
import java.util.*;
import java.time.LocalDate;
import Alerts.Alert;
import Alerts.AlertStorage;
import Alerts.SeverityLevel;
import History.AlertHistory;
import History.AlertHistoryStorage;
import History.ReadingHistory;
import History.ReadingHistoryStorage;
import Readings.GPSReading;
import Readings.NumericalReading;
import Readings.Reading;
import Readings.ReadingStorage;

import ThresHold.ThresholdGPS;
import ThresHold.ThresholdRange;
import Sensors.WaterSensor;
import Sensors.EnvironmentalSensor;
import Sensors.BiometricSensor;
import Sensors.GPSSensor;
import Sensors.Sensor;
import Readings.GPSReading;
import Readings.NumericalReading;
import Alerts.Alert;
import Alerts.SeverityLevel;
import History.AlertHistory;
import History.ReadingHistory;
import ThresHold.ThresholdGPS;
import ThresHold.ThresholdRange;
import ThresHold.ThresholdStorage;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        Farm farm = new Farm();

        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║        Q1 — Manage Farm Zones & Entities     ║");
        System.out.println("╚══════════════════════════════════════════════╝");

        // --- Zones are loaded from src/Inputs/Zone.JSON ---
        Cropzone cropzone = (Cropzone) farm.findZone(123);
        LivestockZone livestock = (LivestockZone) farm.findZone(111);
        AquacultureZone aquacultureZone = (AquacultureZone) farm.findZone(4);

        // --- Edit zone ---
        farm.editZone(1, "South Field", Zonestatus.ACTIVE);

        // --- Deactivate zone ---
        farm.deactivateZone(1);
        farm.editZone(1, "Suspended Zone ", Zonestatus.SUSPENDED); // test: edit suspended zone

        // --- Assign crop to zone ---
        Cropzone crop2 = (Cropzone) farm.findZone(156);
        Map<String, SoilRequirements> soilRequirements = new SoilRequirementStorage().loadSoilRequirements();
        SoilRequirements soilReq = soilRequirements.get("wheat");
        Crop wheat = new Crop(Family.CEREALS, LocalDate.now(), GrowthStage.SOWING, "14/04/2025", soilReq);
        farm.assignCropToZone(156, wheat);
        crop2.display();

        // --- Assign animal to zone ---
        Ruminants ruminant = (Ruminants) farm.findZone(2);
        Map<String, AnimalStorage.AnimalRecord> animals = new AnimalStorage().loadAnimals();
        AnimalStorage.AnimalRecord cowRecord = animals.get("cow");
        Animal cow = cowRecord.animal();
        farm.assignAnimalToZone(cowRecord.zoneCode(), cow);
        ruminant.display();

        // --- Display overview ---
        System.out.println("\n--- Farm Overview ---");
        farm.displayOverview();

        // --- Record production ---
        cropzone.recordProduction(LocalDate.now(), 125);
        cropzone.displayProduction();
        cropzone.displayTotalProduction();
        // production in farm
        farm.displayAllProductionsSummary();

        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║             Q2 — Manage Crops                ║");
        System.out.println("╚══════════════════════════════════════════════╝");

        Cropzone zoneC1 = (Cropzone) farm.findZone(101);
        Cropzone zoneC2 = (Cropzone) farm.findZone(102);

        // --- Register crops ---
        SoilRequirements soil2 = soilRequirements.get("tomato");
        SoilRequirements soil3 = soilRequirements.get("apple");

        Crop tomato = new Crop(Family.VEGETABLES, LocalDate.of(2025, 4, 15),
                GrowthStage.MATURITY, "2025-07-15", soil2);
        Crop apple = new Crop(Family.FRUITS, LocalDate.of(2025, 2, 10),
                GrowthStage.SOWING, "2025-09-01", soil3);

        System.out.println("\n--- Assigning Crops ---");
        farm.assignCropToZone(101, wheat);
        farm.assignCropToZone(101, tomato);
        farm.assignCropToZone(102, apple);
        farm.assignCropToZone(10, apple);// invalid doesn't exist
        farm.assignCropToZone(2, wheat);// ruminant not crop zone

        // ------- Update growth stage ------
        System.out.println("\n--- Update Growth Stage ---");
        wheat.UpdateGrowth(GrowthStage.MATURITY);

        // --- Display growth stages ---
        System.out.println("\n--- Display Growth Stages (zone 101) ---");
        zoneC1.displayGrowthAllCrop();

        // --- Crop status report ---
        System.out.println("\n--- Crop Status Report - Zone 101 ---");
        farm.CropStatusReport(101);
        System.out.println("\n--- Crop Status Report - Zone 102 ---");
        farm.CropStatusReport(102);
        // test for one crop
        tomato.generateStatusReport();

        // --- Test suspended zone ---
        System.out.println("\n--- Test Suspended Zone ---");
        farm.deactivateZone(101);
        farm.assignCropToZone(101, apple);

        // --- Test invalid zone ---
        System.out.println("\n--- Test Invalid Zone Code ---");
        farm.CropStatusReport(999);

        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║           Q3 — Manage Animals                ║");
        System.out.println("╚══════════════════════════════════════════════╝");

        // --- Register animal ---
        System.out.println("\n--- Register Animal ---");
        AnimalStorage.AnimalRecord sheepRecord = animals.get("sheep");
        Animal sheep = sheepRecord.animal();
        farm.assignAnimalToZone(sheepRecord.zoneCode(), sheep);

        // --- Log health events ---
        System.out.println("\n--- Log Health Events ---");
        new HealthEventStorage().applyHealthEvents(animals);
        cow.dispalyAllEvents();

        // --- Define and display feeding schedule per zone ---
        System.out.println("\n---- Feeding Schedule - Livestock Zone ----");

        // Create a LivestockZone
        LivestockZone zoneL = (LivestockZone) farm.findZone(3);

        // Display zone before adding anything
        System.out.println("======= Before Setup =======");
        zoneL.displayFeedingSchedules();

        // Define feeding schedule
        System.out.println("\n======= Defining Schedule =======");
        zoneL.defineFeedingSchedule(List.of("07:00", "12:00", "18:00"));

        // Display feeding schedule after defining it
        System.out.println("\n======= Feeding Schedule =======");
        zoneL.displayFeedingSchedules();

        System.out.println("\n=== Test: Specific Zone Schedule ===");
        farm.displayFeedingSchedule(3); // Cattle Zone
        farm.displayFeedingSchedule(2); // Fish Zone
        farm.displayFeedingSchedule(99); //

        System.out.println("\n=== Test: All Feeding Schedules ===");
        farm.displayAllFeedingSchedules();

        System.out.println("========================Sensors===================================");


        // Create a threshold range for temperature
        System.out.println("Creating sensors and readings...");
        Map<String, ThresholdRange> thresholds = new ThresholdStorage().loadThresholds();
        ThresholdRange tempThreshold = thresholds.get("temperature");
        ThresholdRange humidityThreshold = thresholds.get("humidity");
        ThresholdRange bodyTempThreshold = thresholds.get("bodyTemperature");
        ThresholdGPS gpsThreshold = (ThresholdGPS) thresholds.get("gps");
        ThresholdRange oxThreshold = thresholds.get("dissolvedOxygen");

        System.out.println();
        System.out.println("Creating reading objects...");
        Map<String, Reading> readings = new ReadingStorage().loadReadings(farm, thresholds);
        NumericalReading r = (NumericalReading) readings.get("r");
        NumericalReading r02 = (NumericalReading) readings.get("r02");
        NumericalReading r03 = (NumericalReading) readings.get("r03");
        NumericalReading r04 = (NumericalReading) readings.get("r04");
        NumericalReading r05 = (NumericalReading) readings.get("r05");
        NumericalReading r06 = (NumericalReading) readings.get("r06");


        NumericalReading r1 = (NumericalReading) readings.get("r1");


        NumericalReading r2 = (NumericalReading) readings.get("r2");

        GPSReading r5 = (GPSReading) readings.get("r5");


        GPSReading r6 = (GPSReading) readings.get("r6");

        NumericalReading r7 = (NumericalReading) readings.get("r7");
        NumericalReading r8 = (NumericalReading) readings.get("r8");
        NumericalReading r9 = (NumericalReading) readings.get("r9");
        NumericalReading r10 = (NumericalReading) readings.get("r10");
        GPSReading r11 = (GPSReading) readings.get("r11");
        GPSReading r12 = (GPSReading) readings.get("r12");
        GPSSensor gpsSensorZone2 = r11.getSensor();

        // test alerts
        System.out.println();
        // System.out.println("================================");
        // System.out.println("Alert Tests");
        // System.out.println("================================");
        Map<String, Alert> alerts = new AlertStorage().loadAlerts(readings);
        Alert alert = alerts.get("bodyTemperatureAlert");
        // alert.display();

        showAlertResult("Body temperature alert", alert);

        Alert alert1 = alerts.get("humidityAlert");
        // showAlertResult("Humidity alert", alert1);

        Alert alert2 = alerts.get("bodyTemperatureAlertCopy");
        showAlertResult("Body temperature alert", alert2);

        Alert alert5 = alerts.get("gpsAlertG09");
        // showAlertResult("GPS alert G09", alert5);

        Alert alert6 = alerts.get("gpsAlertG07");
        // showAlertResult("GPS alert G07", alert6);

        AlertHistory alertHistory = new AlertHistoryStorage().loadAlertHistory(alerts);
        alertHistory.displayActivePanelSortedBySeverity();
        alertHistory.alertsHistoryFilterZone(zoneC2);
        alertHistory.alertsHistoryFilterSensorType(gpsSensorZone2);
        alertHistory.alertsHistoryFilterLevel(SeverityLevel.CRITICAL);
        alertHistory.alertsHistoryFilterTimePeriod(
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 31));

        // test filter
        alertHistory.browseAlerts(
                zoneC2,
                gpsSensorZone2,
                SeverityLevel.CRITICAL,
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 31));

        // test reading history
        ReadingHistory waterSensorHistory = new ReadingHistoryStorage().loadReadingHistory(readings);

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
