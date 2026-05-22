package moduls;

import Sensors.EnvironmentalSensor;
import Sensors.GPSSensor;
import Sensors.WaterSensor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AquacultureZone  extends Zones  implements Producible{

     private Tank tank;
     private int numberOfAnimals;
     private  Feedingprogramme feedingprogrammeA;
     private double totalP;
    // private List<ProductionInfo> productionRecords =new ArrayList<>();
     private TreeMap<LocalDate,ProductionInfo> productionRecords=new TreeMap<>();
     private List<WaterSensor> waterSensors = new ArrayList<>();

     public AquacultureZone(int code, String name, Tank tank ,int  numberOfAnimals ,String feedType, int quantitiesPerMeal) {
          super(code, name);
          this.tank = tank;
          this.numberOfAnimals =  numberOfAnimals;
          this.feedingprogrammeA = new Feedingprogramme(feedType, quantitiesPerMeal);
          this.totalP = 0;
     }

     public int getNumberOfAnimals() {
          return numberOfAnimals;
     }
     public Feedingprogramme getFeedingprogrammeA() {
          return feedingprogrammeA;
     }
     public double getTotalP() {
          return totalP;
     }

     public void setTotalP(double totalP) {
          this.totalP = totalP;
     }

     public void setFeedingprogrammeA(Feedingprogramme feedingprogrammeA) {
          this.feedingprogrammeA = feedingprogrammeA;
     }

     public void setNumberOfAnimals(int numberOfAnimals) {
          this.numberOfAnimals = numberOfAnimals;
     }

     public void recordProduction(LocalDate date, double quantity) {
          if (quantity < 0) {
               System.out.println("Quantity cannot be negative.");
               return;
          }
          if(this.getStatus().equals("suspended")){
               System.out.println("Cannot record production for a suspended zone.");
               return;
          }
          ProductionInfo P=new ProductionInfo(quantity,ProductionType.HARVESTWEIGHT,"kg");
          productionRecords.put(date,P);
          this.totalP += quantity ;
          System.out.println("record Production added succesfuly");

     }

     @Override
     public void displayProduction() {
          System.out.println("=== Harvest Weight Records - Zone: " + getName() + " ===");
          if (productionRecords.isEmpty()) {
               System.out.println("  No production records yet.");
               return;
          }

         for(Map.Entry<LocalDate, ProductionInfo> entry : productionRecords.entrySet()) {
             System.out.println(entry.getKey());
             entry.getValue().display();
             System.out.println("_____________________________________________");
         }

          System.out.println("  Total Harvest Weight : " + totalP + " kg");
     }

     @Override
     public void displayTotalProduction() {
          System.out.println("Zone: " + getName()
                  + " | Total Harvest : " + totalP + " kg");
     }



     @Override
     public void display() {
          System.out.println("=== AquacultureZone ===");
          System.out.println("  Code  : " + getUniquecode());
          System.out.println("  Name  : " + getName());
          System.out.println("  Status: " + Status);
          System.out.println("  Number of animals: " + numberOfAnimals);
          feedingprogrammeA.display();
          tank.display();
     }

     public void displayFeedingSchedules() {
          System.out.println("=== Feeding Schedule for Zone: " + getName() + " ===");
          feedingprogrammeA.display();
     }

     public void defineFeedingSchedule(List<String> mealTimes) {
          for (String time : mealTimes) {
               feedingprogrammeA.addMealTime(time);
          }
     }


    public void addWaterSensor(WaterSensor sensor) {
     if(this.getStatus().equals("suspended")){
         System.out.println("Cannot add sensor to a suspended zone.");
         return;
     }
        if (sensor != null) {
            waterSensors.add(sensor);
            System.out.println("GPS sensor [" + sensor.getId() + "] added to zone: " + getName());
        } else {
            System.out.println("Cannot add null sensor.");
        }
    }

    @Override
    protected void suspendSensors() {
        for (WaterSensor s : waterSensors) s.suspend();
    }
    @Override
    protected void activateSensors() {
        for (WaterSensor s : waterSensors) s.activate();
    }

}
