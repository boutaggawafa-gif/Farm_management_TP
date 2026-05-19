package Zones;

public class Zone {
    private String id;
    private String name;

    // Constructor
    public Zone(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
