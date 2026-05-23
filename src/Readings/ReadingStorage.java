package Readings;

import Sensors.BiometricSensor;
import Sensors.EnvironmentalSensor;
import Sensors.GPSSensor;
import Sensors.Sensor;
import Sensors.SensorStorage;
import Sensors.WaterSensor;
import ThresHold.ThresholdGPS;
import ThresHold.ThresholdRange;
import moduls.AquacultureZone;
import moduls.Cropzone;
import moduls.Farm;
import moduls.LivestockZone;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReadingStorage {
    private static final Path READING_FILE = Path.of("src", "Inputs", "Reading.JSON");

    public Map<String, Reading> loadReadings(Farm farm, Map<String, ThresholdRange> thresholds) {
        Map<String, Reading> readings = new LinkedHashMap<>();
        Map<String, SensorStorage.SensorInfo> sensors = new SensorStorage().loadSensors(farm, thresholds);
        try {
            if (!Files.exists(READING_FILE) || Files.size(READING_FILE) == 0) {
                return readings;
            }

            String json = Files.readString(READING_FILE, StandardCharsets.UTF_8).trim();
            if (json.isEmpty() || json.equals("[]")) {
                return readings;
            }

            for (String object : extractObjects(json)) {
                Map<String, String> values = parseObject(object);
                String name = values.get("name");
                Reading reading = toReading(values, sensors);
                if (name != null && reading != null) {
                    readings.put(name, reading);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read readings from " + READING_FILE + ": " + e.getMessage());
        }
        return readings;
    }

    private Reading toReading(Map<String, String> values, Map<String, SensorStorage.SensorInfo> sensors) {
        SensorStorage.SensorInfo sensorInfo = sensors.get(values.get("sensorId"));

        if (sensorInfo == null) {
            return null;
        }

        LocalDate date = LocalDate.parse(values.get("date"));

        switch (sensorInfo.type()) {
            case "water" -> {
                WaterSensor sensor = new WaterSensor(
                        sensorInfo.id(),
                        (AquacultureZone) sensorInfo.zone(),
                        sensorInfo.threshold(),
                        WaterSensor.MeasurementType.valueOf(sensorInfo.measurementType()),
                        date
                );
                sensor.sendReading(parseDouble(values.get("value"), 0));
                return new NumericalReading(sensor, sensorInfo.threshold());
            }
            case "environmental" -> {
                EnvironmentalSensor sensor = new EnvironmentalSensor(
                        sensorInfo.id(),
                        (Cropzone) sensorInfo.zone(),
                        sensorInfo.threshold(),
                        EnvironmentalSensor.MeasurementType.valueOf(sensorInfo.measurementType()),
                        date
                );
                sensor.sendReading(parseDouble(values.get("value"), 0));
                return new NumericalReading(sensor, sensorInfo.threshold());
            }
            case "biometric" -> {
                BiometricSensor sensor = new BiometricSensor(
                        sensorInfo.id(),
                        (LivestockZone) sensorInfo.zone(),
                        sensorInfo.threshold(),
                        BiometricSensor.MeasurementType.valueOf(sensorInfo.measurementType()),
                        date
                );
                sensor.sendReading(parseDouble(values.get("value"), 0));
                return new NumericalReading(sensor, sensorInfo.threshold());
            }
            case "gps" -> {
                GPSSensor sensor = new GPSSensor(
                        sensorInfo.id(),
                        (LivestockZone) sensorInfo.zone(),
                        (ThresholdGPS) sensorInfo.threshold(),
                        date
                );
                sensor.sendReading(
                        parseDouble(values.get("latitude"), 0),
                        parseDouble(values.get("longitude"), 0)
                );
                return new GPSReading(sensor, (ThresholdGPS) sensorInfo.threshold());
            }
            default -> {
                System.out.println("Unknown sensor type in Sensor.JSON: " + sensorInfo.type());
                return null;
            }
        }
    }

    private List<String> extractObjects(String json) {
        List<String> objects = new ArrayList<>();
        int depth = 0;
        int start = -1;
        boolean inString = false;
        boolean escaped = false;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (escaped) {
                escaped = false;
                continue;
            }
            if (c == '\\') {
                escaped = true;
                continue;
            }
            if (c == '"') {
                inString = !inString;
                continue;
            }
            if (inString) {
                continue;
            }
            if (c == '{') {
                if (depth == 0) {
                    start = i;
                }
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start >= 0) {
                    objects.add(json.substring(start, i + 1));
                }
            }
        }
        return objects;
    }

    private Map<String, String> parseObject(String object) {
        Map<String, String> values = new LinkedHashMap<>();
        String body = object.substring(1, object.length() - 1);
        for (String pair : splitPairs(body)) {
            int separator = pair.indexOf(':');
            if (separator < 0) {
                continue;
            }
            String key = unquote(pair.substring(0, separator).trim());
            String value = unquote(pair.substring(separator + 1).trim());
            values.put(key, value);
        }
        return values;
    }

    private List<String> splitPairs(String body) {
        List<String> pairs = new ArrayList<>();
        int start = 0;
        boolean inString = false;
        boolean escaped = false;

        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if (escaped) {
                escaped = false;
                continue;
            }
            if (c == '\\') {
                escaped = true;
                continue;
            }
            if (c == '"') {
                inString = !inString;
                continue;
            }
            if (c == ',' && !inString) {
                pairs.add(body.substring(start, i));
                start = i + 1;
            }
        }
        pairs.add(body.substring(start));
        return pairs;
    }

    private double parseDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }

    private String unquote(String value) {
        String trimmed = value.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
