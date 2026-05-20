package moduls;

public class AquacultureSpecies {
    private String name;
    private String type;   // fish, shrimp, etc.

    public AquacultureSpecies(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public void display() {
        System.out.println("    Species: " + name + " (" + type + ")");
    }
}