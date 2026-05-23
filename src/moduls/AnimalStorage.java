package moduls;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnimalStorage {
    private static final Path ANIMAL_FILE = Path.of("src", "Inputs", "Animal.JSON");

    public Map<String, AnimalRecord> loadAnimals() {
        Map<String, AnimalRecord> animals = new LinkedHashMap<>();
        try {
            if (!Files.exists(ANIMAL_FILE) || Files.size(ANIMAL_FILE) == 0) {
                return animals;
            }

            String json = Files.readString(ANIMAL_FILE, StandardCharsets.UTF_8).trim();
            if (json.isEmpty() || json.equals("[]")) {
                return animals;
            }

            for (String object : extractObjects(json)) {
                Map<String, String> values = parseObject(object);
                String name = values.get("name");
                AnimalRecord record = toAnimalRecord(values);
                if (name != null && record != null) {
                    animals.put(name, record);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read animals from " + ANIMAL_FILE + ": " + e.getMessage());
        }
        return animals;
    }

    private AnimalRecord toAnimalRecord(Map<String, String> values) {
        try {
            Animal animal = new Animal(
                    parseInt(values.get("uniqueNumber"), 0),
                    values.getOrDefault("species", "Unknown"),
                    parseInt(values.get("age"), 0),
                    HealthStatus.valueOf(values.getOrDefault("health", "HEALTHY")),
                    parseDouble(values.get("weight"), 0)
            );
            return new AnimalRecord(animal, parseInt(values.get("zoneCode"), 0));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid animal in Animal.JSON: " + e.getMessage());
            return null;
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

    private int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (RuntimeException e) {
            return defaultValue;
        }
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

    public record AnimalRecord(Animal animal, int zoneCode) {
    }
}
