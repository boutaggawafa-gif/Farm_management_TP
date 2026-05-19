package History;

import Readings.Reading;
import Readings.ReadingStatus;
import Sensors.Sensor;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class ReadingHistory {
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final int MAX_BAR_WIDTH = 40;

    TreeMap<LocalDate, Reading> readingsHistory = new TreeMap<LocalDate, Reading>();

    public TreeMap<LocalDate, Reading> getReadingsHistory() {
        return readingsHistory;
    }

    public void display() {
        System.out.println("================================");
        System.out.println("Reading History");
        System.out.println("================================");

        if (readingsHistory.isEmpty()) {
            System.out.println("No readings found.");
            return;
        }

        readingsHistory.forEach((date, reading) -> {
            System.out.println("--------------------------------");
            System.out.println("Date: " + date);
            reading.display();
        });
    }


    public void filterRange(LocalDate startDate , LocalDate endDate){
        if (startDate.isAfter(endDate)) {
            System.out.println("Invalid date range: start date must be before end date.");
            return;
        }

        TreeMap<LocalDate, Reading> filteredReadings = new TreeMap<>(
            readingsHistory.subMap(startDate, true, endDate, true)
        );

        System.out.println("================================");
        System.out.println("Reading History From " + startDate + " To " + endDate);
        System.out.println("================================");

        if (filteredReadings.isEmpty()) {
            System.out.println("No readings found in this date range.");
            return;
        }

        filteredReadings.forEach((date, reading) -> {
            System.out.println("--------------------------------");
            System.out.println("Date: " + date);
            reading.display();
        });

    }

    public void displayDashboardByZone() {
        System.out.println("==============================================");
        System.out.println("Reading Dashboard By Zone");
        System.out.println("==============================================");

        if (readingsHistory.isEmpty()) {
            System.out.println("No readings found.");
            return;
        }

        TreeMap<String, TreeMap<LocalDate, Reading>> readingsByZone = new TreeMap<>();

        readingsHistory.forEach((date, reading) -> {
            Sensor sensor = reading.getSensor();
            String zoneName = sensor.getLocation().getName();
            readingsByZone.putIfAbsent(zoneName, new TreeMap<>());
            readingsByZone.get(zoneName).put(date, reading);
        });

        readingsByZone.forEach((zoneName, zoneReadings) -> {
            System.out.println();
            System.out.println("Zone: " + zoneName);
            System.out.println("----------------------------------------------");
            System.out.printf("%-12s %-12s %-20s %-20s%n", "Date", "Sensor", "Reading Type", "Status");
            System.out.println("----------------------------------------------");

            for (Map.Entry<LocalDate, Reading> entry : zoneReadings.entrySet()) {
                Reading reading = entry.getValue();
                Sensor sensor = reading.getSensor();
                ReadingStatus status = reading.evaluate();

                System.out.printf(
                    "%-12s %-12s %-20s %-20s%n",
                    entry.getKey(),
                    sensor.getId(),
                    reading.getClass().getSimpleName(),
                    colorStatus(status)
                );
            }
        });
    }

    private String colorStatus(ReadingStatus status) {
        if (status == ReadingStatus.NORMAL) {
            return GREEN + "NORMAL" + RESET;
        }
        if (status == ReadingStatus.WARNING) {
            return YELLOW + "WARNING" + RESET;
        }
        return RED + "CRITICAL" + RESET;
    }

    public void displayEvolutionChartBySensor() {
        System.out.println("==============================================");
        System.out.println("Reading Evolution Chart By Sensor");
        System.out.println("==============================================");

        if (readingsHistory.isEmpty()) {
            System.out.println("No readings found.");
            return;
        }

        TreeMap<String, TreeMap<LocalDate, Reading>> readingsBySensor = new TreeMap<>();

        readingsHistory.forEach((date, reading) -> {
            String sensorId = reading.getSensor().getId();
            readingsBySensor.putIfAbsent(sensorId, new TreeMap<>());
            readingsBySensor.get(sensorId).put(date, reading);
        });

        readingsBySensor.forEach((sensorId, sensorReadings) -> {
            double maxValue = maxChartValue(sensorReadings);

            System.out.println();
            System.out.println("Sensor: " + sensorId);
            System.out.println("----------------------------------------------");

            sensorReadings.forEach((date, reading) -> printChartRow(date.toString(), reading, maxValue));
        });
    }

    public void displayEvolutionChartByZone() {
        System.out.println("==============================================");
        System.out.println("Reading Evolution Chart By Zone");
        System.out.println("==============================================");

        if (readingsHistory.isEmpty()) {
            System.out.println("No readings found.");
            return;
        }

        TreeMap<String, TreeMap<LocalDate, Reading>> readingsByZone = new TreeMap<>();

        readingsHistory.forEach((date, reading) -> {
            String zoneName = reading.getSensor().getLocation().getName();
            readingsByZone.putIfAbsent(zoneName, new TreeMap<>());
            readingsByZone.get(zoneName).put(date, reading);
        });

        readingsByZone.forEach((zoneName, zoneReadings) -> {
            double maxValue = maxChartValue(zoneReadings);

            System.out.println();
            System.out.println("Zone: " + zoneName);
            System.out.println("----------------------------------------------");

            zoneReadings.forEach((date, reading) -> {
                String label = date + " " + reading.getSensor().getId();
                printChartRow(label, reading, maxValue);
            });
        });
    }

    private double maxChartValue(TreeMap<LocalDate, Reading> readings) {
        double maxValue = 0;

        for (Reading reading : readings.values()) {
            maxValue = Math.max(maxValue, reading.getChartValue());
        }

        return maxValue;
    }

    private void printChartRow(String label, Reading reading, double maxValue) {
        double value = reading.getChartValue();
        int barWidth = calculateBarWidth(value, maxValue);
        String bar = buildBar(barWidth);
        String status = colorStatus(reading.evaluate());

        System.out.printf(
            "%-18s | %-40s %8.2f  %s%n",
            label,
            bar,
            value,
            status
        );
    }

    private int calculateBarWidth(double value, double maxValue) {
        if (maxValue <= 0) {
            return 0;
        }

        return Math.max(1, (int) Math.round((value / maxValue) * MAX_BAR_WIDTH));
    }

    private String buildBar(int width) {
        StringBuilder bar = new StringBuilder();

        for (int i = 0; i < width; i++) {
            bar.append("#");
        }

        return bar.toString();
    }
}
