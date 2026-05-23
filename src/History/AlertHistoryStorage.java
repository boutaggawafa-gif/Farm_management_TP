package History;

import Alerts.Alert;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AlertHistoryStorage {
    private static final Path ALERT_HISTORY_FILE = Path.of("src", "Inputs", "AlertHistory.JSON");

    public AlertHistory loadAlertHistory(Map<String, Alert> alerts) {
        AlertHistory alertHistory = new AlertHistory();
        try {
            if (!Files.exists(ALERT_HISTORY_FILE) || Files.size(ALERT_HISTORY_FILE) == 0) {
                return alertHistory;
            }

            String json = Files.readString(ALERT_HISTORY_FILE, StandardCharsets.UTF_8).trim();
            if (json.isEmpty() || json.equals("[]")) {
                return alertHistory;
            }

            for (String object : extractObjects(json)) {
                Map<String, String> values = parseObject(object);
                Alert alert = alerts.get(values.get("alert"));
                if (alert != null) {
                    alertHistory.addAlert(alert);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read alert history from " + ALERT_HISTORY_FILE + ": " + e.getMessage());
        }
        return alertHistory;
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

    private String unquote(String value) {
        String trimmed = value.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
