package moduls;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ZoneStorage {
    private static final Path ZONE_FILE = Path.of("src", "Inputs", "Zone.JSON");

    public List<Zones> loadZones() {
        try {
            if (!Files.exists(ZONE_FILE) || Files.size(ZONE_FILE) == 0) {
                return new ArrayList<>();
            }

            String json = Files.readString(ZONE_FILE, StandardCharsets.UTF_8).trim();
            if (json.isEmpty() || json.equals("[]")) {
                return new ArrayList<>();
            }

            List<Zones> zones = new ArrayList<>();
            for (String object : extractObjects(json)) {
                Zones zone = toZone(parseObject(object));
                if (zone != null) {
                    zones.add(zone);
                }
            }
            return zones;
        } catch (IOException e) {
            System.out.println("Could not read zones from " + ZONE_FILE + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveZones(List<Zones> zones) {
        try {
            Files.createDirectories(ZONE_FILE.getParent());
            Files.writeString(ZONE_FILE, toJson(zones), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Could not save zones to " + ZONE_FILE + ": " + e.getMessage());
        }
    }

    private String toJson(List<Zones> zones) {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < zones.size(); i++) {
            Zones zone = zones.get(i);
            json.append("  {\n");
            json.append("    \"type\": \"").append(escape(zone.getClass().getSimpleName())).append("\",\n");
            json.append("    \"code\": ").append(zone.getUniquecode()).append(",\n");
            json.append("    \"name\": \"").append(escape(zone.getName())).append("\",\n");
            json.append("    \"status\": \"").append(zone.getStatus()).append("\"");

            if (zone instanceof LivestockZone livestockZone) {
                Feedingprogramme feeding = livestockZone.getFeedingprogramme();
                json.append(",\n    \"feedType\": \"").append(escape(feeding.getFeedType())).append("\",\n");
                json.append("    \"quantitiesPerMeal\": ").append(feeding.getQuantitiesPerMeal());
            } else if (zone instanceof AquacultureZone aquacultureZone) {
                Feedingprogramme feeding = aquacultureZone.getFeedingprogrammeA();
                json.append(",\n    \"numberOfAnimals\": ").append(aquacultureZone.getNumberOfAnimals()).append(",\n");
                json.append("    \"feedType\": \"").append(escape(feeding.getFeedType())).append("\",\n");
                json.append("    \"quantitiesPerMeal\": ").append(feeding.getQuantitiesPerMeal());
            }

            json.append("\n  }");
            if (i < zones.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]\n");
        return json.toString();
    }

    private Zones toZone(Map<String, String> values) {
        String type = values.getOrDefault("type", "Cropzone");
        int code = parseInt(values.get("code"), 0);
        String name = values.getOrDefault("name", "Unnamed zone");
        String feedType = values.getOrDefault("feedType", "Not specified");
        int quantitiesPerMeal = parseInt(values.get("quantitiesPerMeal"), 0);

        Zones zone;
        switch (type) {
            case "LivestockZone" -> zone = new LivestockZone(code, name, feedType, quantitiesPerMeal);
            case "Ruminants" -> zone = new Ruminants(code, name, feedType, quantitiesPerMeal);
            case "Poultry" -> zone = new Poultry(code, name, feedType, quantitiesPerMeal);
            case "AquacultureZone" -> zone = new AquacultureZone(
                    code,
                    name,
                    new Tank(),
                    parseInt(values.get("numberOfAnimals"), 0),
                    feedType,
                    quantitiesPerMeal);
            case "Cropzone" -> zone = new Cropzone(code, name);
            default -> {
                System.out.println("Unknown zone type in Zone.JSON: " + type);
                return null;
            }
        }

        try {
            zone.setStatus(Zonestatus.valueOf(values.getOrDefault("status", "ACTIVE")));
        } catch (IllegalArgumentException e) {
            zone.setStatus(Zonestatus.ACTIVE);
        }
        return zone;
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
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String unquote(String value) {
        String trimmed = value.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
