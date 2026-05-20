package History;

import Alerts.Alert;
import Alerts.SeverityLevel;
import Readings.Reading;
import Sensors.Sensor;
import Zones.Zone;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

public class AlertHistory {
    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";

    TreeMap<LocalDate, ArrayList<Alert>> alertsHistory = new TreeMap<LocalDate, ArrayList<Alert>>();

    public void addAlert(Alert alert) {
        LocalDate date = alert.getReading().getSensor().getDate();
        alertsHistory.putIfAbsent(date, new ArrayList<>());
        alertsHistory.get(date).add(alert);
    }

    public TreeMap<LocalDate, ArrayList<Alert>> getAlertsHistory() {
        return alertsHistory;
    }

    public void displayActivePanelSortedBySeverity() {
        List<AlertPanelRow> activeAlerts = new ArrayList<>();

        alertsHistory.forEach((date, alerts) -> {
            for (Alert alert : alerts) {
                if (alert.lunchAlert()) {
                    activeAlerts.add(new AlertPanelRow(date, alert));
                }
            }
        });

        activeAlerts.sort(
                Comparator.comparingInt((AlertPanelRow row) -> severityRank(row.alert.getLevel()))
                        .thenComparing(row -> row.date));

        System.out.println("==============================================================");
        System.out.println("Active Alerts Panel");
        System.out.println("==============================================================");

        if (activeAlerts.isEmpty()) {
            System.out.println("No active alerts.");
            return;
        }

        System.out.printf("%-12s %-10s %-12s %-20s %-16s %-12s%n", "Date", "Sensor", "Zone", "Reading Type",
                "Value", "Severity");
        System.out.println("--------------------------------------------------------------");

        for (AlertPanelRow row : activeAlerts) {
            Reading reading = row.alert.getReading();
            Sensor sensor = reading.getSensor();

            System.out.printf(
                    "%-12s %-10s %-12s %-20s %-16s %-12s%n",
                    row.date,
                    sensor.getId(),
                    sensor.getLocation().getName(),
                    reading.getClass().getSimpleName(),
                    formatValueWithUnit(reading),
                    colorSeverity(row.alert.getLevel()));
        }
    }

    private int severityRank(SeverityLevel level) {
        if (level == SeverityLevel.CRITICAL) {
            return 0;
        }
        if (level == SeverityLevel.WARNING) {
            return 1;
        }
        return 2;
    }

    private String colorSeverity(SeverityLevel level) {
        if (level == SeverityLevel.CRITICAL) {
            return RED + "CRITICAL" + RESET;
        }
        if (level == SeverityLevel.WARNING) {
            return YELLOW + "WARNING" + RESET;
        }
        return "NORMAL";
    }

    private static class AlertPanelRow {
        private final LocalDate date;
        private final Alert alert;

        private AlertPanelRow(LocalDate date, Alert alert) {
            this.date = date;
            this.alert = alert;
        }
    }

    public void alertsHistoryFilterZone(Zone zone) {
        browseAlerts(
                "Alert History - Zone: " + zone.getName(),
                zone,
                null,
                null,
                null,
                null);
    }

    public void alertsHistoryFilterSensorType(Sensor sensor) {
        browseAlerts(
                "Alert History - Sensor Type: " + sensor.getClass().getSimpleName(),
                null,
                sensor,
                null,
                null,
                null);
    }

    public void alertsHistoryFilterLevel(SeverityLevel level) {
        browseAlerts(
                "Alert History - Level: " + level,
                null,
                null,
                level,
                null,
                null);
    }

    public void alertsHistoryFilterTimePeriod(LocalDate start, LocalDate end) {
        browseAlerts(
                "Alert History - From " + start + " To " + end,
                null,
                null,
                null,
                start,
                end);
    }

    public void browseAlerts(Zone zone, Sensor sensorType, SeverityLevel level, LocalDate start, LocalDate end) {
        browseAlerts("Filtered Alert History", zone, sensorType, level, start, end);
    }

    private void browseAlerts(String title, Zone zone, Sensor sensorType, SeverityLevel level, LocalDate start,
            LocalDate end) {
        List<AlertPanelRow> filteredAlerts = new ArrayList<>();

        if (start != null && end != null && start.isAfter(end)) {
            System.out.println("Invalid date range: start date must be before end date.");
            return;
        }

        TreeMap<LocalDate, ArrayList<Alert>> source = alertsHistory;
        if (start != null && end != null) {
            source = new TreeMap<>(alertsHistory.subMap(start, true, end, true));
        }

        source.forEach((date, alerts) -> {
            for (Alert alert : alerts) {
                if (matchesFilters(alert, zone, sensorType, level)) {
                    filteredAlerts.add(new AlertPanelRow(date, alert));
                }
            }
        });

        filteredAlerts.sort(
                Comparator.comparingInt((AlertPanelRow row) -> severityRank(row.alert.getLevel()))
                        .thenComparing(row -> row.date));

        displayAlertRows(title, filteredAlerts);
    }

    private boolean matchesFilters(Alert alert, Zone zone, Sensor sensorType, SeverityLevel level) {
        Reading reading = alert.getReading();
        Sensor alertSensor = reading.getSensor();
        alert.evaluate(reading);

        if (zone != null && !alertSensor.getLocation().getId().equals(zone.getId())) {
            return false;
        }

        if (sensorType != null && !alertSensor.getClass().equals(sensorType.getClass())) {
            return false;
        }

        if (level != null && alert.getLevel() != level) {
            return false;
        }

        return true;
    }

    private void displayAlertRows(String title, List<AlertPanelRow> rows) {
        System.out.println("==============================================================");
        System.out.println(title);
        System.out.println("==============================================================");

        if (rows.isEmpty()) {
            System.out.println("No alerts found.");
            return;
        }

        System.out.printf("%-12s %-10s %-12s %-18s %-20s %-16s %-12s%n", "Date", "Sensor", "Zone",
                "Sensor Type", "Reading Type", "Value", "Severity");
        System.out.println("--------------------------------------------------------------");

        for (AlertPanelRow row : rows) {
            Reading reading = row.alert.getReading();
            Sensor sensor = reading.getSensor();

            System.out.printf(
                    "%-12s %-10s %-12s %-18s %-20s %-16s %-12s%n",
                    row.date,
                    sensor.getId(),
                    sensor.getLocation().getName(),
                    sensor.getClass().getSimpleName(),
                    reading.getClass().getSimpleName(),
                    formatValueWithUnit(reading),
                    colorSeverity(row.alert.getLevel()));
        }
    }

    private String formatValueWithUnit(Reading reading) {
        return String.format("%.2f %s", reading.getChartValue(), reading.getUnit());
    }
}
