package Sensors;

import Exception.ThresholdIsNullException;
import Exception.ZoneIsNullException;
import ThresHold.ThresholdRange;
import moduls.Farm;
import moduls.Zones;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SensorStorage {
    private static final Path SENSOR_FILE = Path.of("src", "Inputs", "Sensor.JSON");

    public Map<String, SensorInfo> loadSensors(Farm farm, Map<String, ThresholdRange> thresholds) {
        Map<String, SensorInfo> sensors = new LinkedHashMap<>();
        try {
            if (!Files.exists(SENSOR_FILE) || Files.size(SENSOR_FILE) == 0) {
                return sensors;
            }

            String json = Files.readString(SENSOR_FILE, StandardCharsets.UTF_8).trim();
            if (json.isEmpty() || json.equals("[]")) {
                return sensors;
            }

            for (String object : extractObjects(json)) {
                Map<String, String> values = parseObject(object);
                SensorInfo sensor = toSensorInfo(values, farm, thresholds);
                if (sensor != null) {
                    sensors.put(sensor.id(), sensor);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read sensors from " + SENSOR_FILE + ": " + e.getMessage());
        }
        return sensors;
    }

    private SensorInfo toSensorInfo(Map<String, String> values, Farm farm, Map<String, ThresholdRange> thresholds) {
        String id = values.get("id");
        String type = values.get("type");
        String thresholdName = values.get("threshold");
        Zones zone = farm.findZone(parseInt(values.get("zoneCode"), 0));
        ThresholdRange threshold = thresholds.get(thresholdName);

        try {
            if (zone == null) {
                throw new ZoneIsNullException("Zone is null for sensor [" + id + "].");
            }
            if (threshold == null) {
                throw new ThresholdIsNullException("Threshold [" + thresholdName + "] is null for sensor [" + id + "].");
            }
        } catch (ZoneIsNullException | ThresholdIsNullException e) {
            System.out.println(e.getMessage());
            return null;
        }

        if (id == null || type == null) {
            return null;
        }

        return new SensorInfo(
                id,
                type,
                zone,
                threshold,
                values.getOrDefault("measurementType", "")
        );
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

    private int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
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

    public record SensorInfo(
            String id,
            String type,
            Zones zone,
            ThresholdRange threshold,
            String measurementType
    ) {
    }
}
