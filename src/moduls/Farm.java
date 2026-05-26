package moduls;
import jdk.jshell.Snippet;

import java.time.LocalDate;
import java.util.*;
public class Farm {
    private List<Zones> zones =new ArrayList<>();

    //add a zone
    public void addZone(Zones zone ,TypeZone type) {
        Zones z = findZone(zone.getName());

        if (z != null) {
            System.out.println("Zone with name [" + zone.getName() + "] already exists.");
            return;
        }
        if(zone ==null){
            System.out.println("Invalid zone. Cannot add null.");
            return;
        }
            if(type==TypeZone.CROP_ZONE){
                zones.add((Cropzone) zone);
            }else if(type==TypeZone.LIVESTOCK_ZONE){
                zones.add((LivestockZone) zone);
            }else if(type==TypeZone.AQUACULTURE_ZONE){
                zones.add((AquacultureZone) zone);
            }

        System.out.println("Zone [" + zone.getName() + "] added successfully.");
    }

    // Edit a zone
    public void editZone(String zoneName, String newName, Zonestatus status) {
        Zones z = findZone(zoneName);

        if (z == null) {
            System.out.println("Zone not found.");
            return;
        }
            System.out.println("Zone [" + z.getName() + "] updated:");
            System.out.println("  Name   : " + z.getName() + " → " + newName);
            z.setName(newName);
            System.out.println("  Status : " + z.getStatus() + " → " + status);
            z.setStatus(status);

    }



    //Deactivate a zone
    public void deactivateZone(String zoneName){
        Zones z = findZone(zoneName);
        if (z != null) {
            z.suspend();
        } else {
            System.out.println("Zone not found.");
        }
    }



    //search or find a zone
    public Zones findZone(String zoneName) {

    if (zoneName == null) {
        return null;
    }

    String normalizedZoneName = zoneName.trim().toLowerCase();

    for (Zones z : zones) {

        String zone = z.getName().trim().toLowerCase();

        if (zone.equals(normalizedZoneName)) {
            return z;
        }
    }

    return null;
}

    // Assign crop to CropZone
    public void assignCropToZone(String zoneName, Crop crop) {
        Zones z = findZone(zoneName);

        if (z == null) {
            System.out.println("Zone not found.");
            return;
        }

        if (!(z instanceof Cropzone)) {
            System.out.println("Zone [" + z.getName() +
                    "] is not a CropZone.");
            return;
        }

        if (!z.isActive()) {
            System.out.println("Zone [" + z.getName() +
                    "] is SUSPENDED. Cannot assign crops.");
            return;
        }

        ((Cropzone) z).addCrop(crop);
        System.out.println("Crop assigned to zone [" + z.getName() + "]");
    }


    //  Assign animal to LivestockZone
    public void assignAnimalToZone(String zoneName, Animal animal) {
        Zones z = findZone(zoneName);

        if (z == null) {
            System.out.println("Zone not found.");
            return;
        }

        if (!(z instanceof LivestockZone)) {
            System.out.println("Zone [" + z.getName() +
                    "] is not a LivestockZone.");
            return;
        }

        if (!z.isActive()) {
            System.out.println("Zone [" + z.getName() +
                    "] is SUSPENDED. Cannot assign animals.");
            return;
        }
        ((LivestockZone) z).addAnimal(animal);
        System.out.println("Animal assigned to zone [" + z.getName() + "]");
    }


    //display overview of all zones with their status and the number of hosted entities
    public void displayOverview() {
        System.out.println("========== FARM OVERVIEW ==========");
        System.out.println("Total zones: " + zones.size());
        System.out.println("===================================");

        for (Zones z : zones) {
            System.out.println("Zone  : " + z.getName());
            System.out.println("Code  : " + z.getUniquecode());
            System.out.println("Status: " + z.getStatus());

            // number of hosted entities depends on zone type
            if (z instanceof Cropzone) {
                Cropzone c = (Cropzone) z;
                System.out.println("Crops : " + c.getCropsCount());

            } else if (z instanceof LivestockZone) {
                LivestockZone l = (LivestockZone) z;
                System.out.println("Animals: " + l.getAnimalscount());

            } else if (z instanceof AquacultureZone) {
                AquacultureZone a = (AquacultureZone) z;
                System.out.println("Species: " + a.getNumberOfAnimals());
            }

            System.out.println("-----------------------------------");
        }
    }


    //record production
    public void recordProduction(String zoneName, LocalDate date, double quantity) {
        Zones z = findZone(zoneName);
        if (z == null) {
            System.out.println("Zone not found.");
            return;
        }
        if (!z.isActive()) {
            System.out.println("Zone [" + z.getName() + "] is SUSPENDED.");
            return;
        }
        if (!(z instanceof Producible)) {
            System.out.println("Zone does not support production.");
            return;
        }
        ((Producible) z).recordProduction(date, quantity);
    }


    //display all productions
    public void displayAllProductionsSummary() {
        System.out.println("======= PRODUCTION SUMMARY =======");
        for (Zones z : zones) {
            if (z instanceof Producible) {
                ((Producible) z).displayTotalProduction();
                System.out.println("---------------------------------");
            }
        }
    }

    //Generate a crop status report per zone
    public void CropStatusReport(String zoneName){
        Zones zone=findZone(zoneName);

        if(zone==null){
            System.out.println("zone with name "+zoneName+"does not exist ");
           return;
        }
        if (zone instanceof Cropzone){
            ((Cropzone) zone).generateCropStatusReport();
        }
    }

    //feeding schedules

    // Display feeding schedule for a specific zone by code
    public void displayFeedingSchedule(String zonename) {
        Zones z = findZone(zonename);

        if (z == null) {
            System.out.println("Zone not found.");
            return;
        }

        if (z instanceof LivestockZone) {
            ((LivestockZone) z).displayFeedingSchedules();
        } else if (z instanceof AquacultureZone) {
            ((AquacultureZone) z).displayFeedingSchedules();
        } else {
            System.out.println("Zone [" + z.getName() + "] does not have a feeding schedule.");
        }
    }


    // Display feeding schedules for ALL feedable zones
    public void displayAllFeedingSchedules() {
        System.out.println("======= ALL FEEDING SCHEDULES =======");
        boolean found = false;

        for (Zones z : zones) {
            if (z instanceof LivestockZone) {
                ((LivestockZone) z).displayFeedingSchedules();
                System.out.println("-------------------------------------");
                found = true;
            } else if (z instanceof AquacultureZone) {
                ((AquacultureZone) z).displayFeedingSchedules();
                System.out.println("-------------------------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No feedable zones found.");
        }
    }


}
