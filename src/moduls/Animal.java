package moduls;
import java.time.LocalDate;
import java.util.List;
import java.util.*;

public class Animal { ;
    private int uniqueNumber;
    private String species;
    private int age;
    private double weight;
    private HealthStatus health;
    private TreeMap<LocalDate,HealthEvent> healthEvents = new TreeMap<>();

   // private boolean hasGPSCollar;
  //  private double activityLevel;
   // private double bodyTemperature;

    public Animal(int uniqueNumber, String species, int age, HealthStatus health, double weight) {
        this.uniqueNumber = uniqueNumber;
        this.species = species;
        this.age = age;
        this.health = health;
        this.weight = weight;
    }

    public int getUniqueNumber() {
        return uniqueNumber;
    }

    public void display() {
        System.out.println("  Animal #" + uniqueNumber);
        System.out.println("    Species : " + species);
        System.out.println("    Age     : " + age);
        System.out.println("    Weight  : " + weight + " kg");
        System.out.println("    Health  : " + health);
    }

    public  void addHealthEvent(HealthEvent E,String date){//  date in this form yyyy-MM-dd
        if (E == null) {
            System.out.println("Invalid health event.");
            return;
        }
        try {
            LocalDate localDate = LocalDate.parse(date); // yyyy-MM-dd
            healthEvents.put(localDate,E);
            System.out.println("Health event added successfully.");
        } catch (Exception e) {
            System.out.println("Invalid date format. Use yyyy-MM-dd.");
        }
    }


    public void dispalyAllEvents(){
        System.out.println("================Log Health Event of Animal "+this.uniqueNumber+"===================");
        for(Map.Entry<LocalDate, HealthEvent> entry : healthEvents.entrySet()) {
            System.out.println(entry.getKey());
            entry.getValue().displayHealthEvent();
            System.out.println("_____________________________________________");
        }
        System.out.println("=============================================================================");
    }


}

