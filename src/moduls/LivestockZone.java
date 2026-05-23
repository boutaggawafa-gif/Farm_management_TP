package moduls;
import Exception.ZoneSuspendedException;
import Sensors.BiometricSensor;
import Sensors.GPSSensor;
import ThresHold.ThresholdGPS;

import java.util.*;
public class LivestockZone extends Zones{
    private List<Animal>  animals;
    private Feedingprogramme feedingprogramme;



    public LivestockZone(int code, String name, String feedType, int quantitiesPerMeal) {
        super(code, name);
        this.animals = new ArrayList<>();
        this.feedingprogramme = new Feedingprogramme(feedType,quantitiesPerMeal);
    }


    public Feedingprogramme getFeedingprogramme() {
        return feedingprogramme;
    }
    public void setFeedingprogramme(Feedingprogramme feedingprogramme) {this.feedingprogramme = feedingprogramme;}

    public List<Animal> getAnimals() {return animals;}
    private List<BiometricSensor> biometricSensors = new ArrayList<>();
    private List<GPSSensor> gpsSensors = new ArrayList<>();
    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public void addAnimal(Animal animal){
        try {
            requireActive();
        } catch (ZoneSuspendedException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (animal!= null){
            animals.add(animal);
        }
    }

    public int getAnimalscount(){
      return getAnimals().size();
    }

    @Override
    public void display() {
        System.out.println("=== LivestockZone ===");
        System.out.println("  Code   : " + getUniquecode());
        System.out.println("  Name   : " + getName());
        System.out.println("  Status : " + Status);
        feedingprogramme.display();
        System.out.println("  Animals:");
        if (animals.isEmpty()) {
            System.out.println("    No animals yet.");
        } else {
            for (Animal a : animals) {
                a.display();
            }
        }
    }

    public void addHealthEventToAnimal(int uniqueNumber, HealthEvent event, String date) {
        try {
            requireActive();
        } catch (ZoneSuspendedException e) {
            System.out.println(e.getMessage());
            return;
        }
        for (Animal animal : animals) {
            if (animal.getUniqueNumber() == uniqueNumber) {
                animal.addHealthEvent(event, date);
                System.out.println("Health event added Successfully");
                return;
            }
        }
        System.out.println("Animal #" + uniqueNumber + " not found.");
    }

    public void displayFeedingSchedules() {
        System.out.println("=== Feeding Schedule for Zone: " + getName() + " ===");
        feedingprogramme.display();
    }

    public void defineFeedingSchedule(List<String> mealTimes) {
        for (String time : mealTimes) {
            feedingprogramme.addMealTime(time);
        }
    }

    public void addBiometricSensor(BiometricSensor sensor) {
        try {
            requireActive();
        } catch (ZoneSuspendedException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (sensor != null) {
            biometricSensors.add(sensor);
            System.out.println("Biometric sensor [" + sensor.getId()+ "] added to zone: " + getName());
        } else {
            System.out.println("Cannot add null sensor.");
        }
    }

    public void addGPSSensor(GPSSensor sensor) {
        try {
            requireActive();
        } catch (ZoneSuspendedException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (sensor != null) {
            gpsSensors.add(sensor);
            System.out.println("GPS sensor [" + sensor.getId() + "] added to zone: " + getName());
        } else {
            System.out.println("Cannot add null sensor.");
        }
    }

    @Override
    protected void suspendSensors() {
        for (BiometricSensor s : biometricSensors) s.suspend();
        for (GPSSensor s : gpsSensors) s.suspend();
    }

    @Override
    protected void activateSensors() {
        for (BiometricSensor s : biometricSensors) s.activate();
        for (GPSSensor s : gpsSensors) s.activate();
    }

    private void requireActive() throws ZoneSuspendedException {
        if (!isActive()) {
            throw new ZoneSuspendedException("Zone [" + getName() + "] is SUSPENDED.");
        }
    }


}
