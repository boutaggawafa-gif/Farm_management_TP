package moduls;
import jdk.jshell.Snippet;

import java.time.LocalDate;
import java.util.*;
public class Farm {
    private List<Zones> zones =new ArrayList<>();
    private ZoneStorage zoneStorage = new ZoneStorage();

    public Farm() {
        zones.addAll(zoneStorage.loadZones());
    }

    //add a zone
    public void addZone(Zones zone) {
        if (zone != null) {
            if (findZone(zone.getUniquecode()) != null) {
                System.out.println("Zone with code " + zone.getUniquecode() + " already exists.");
                return;
            }
            zones.add(zone);
            saveZones();
            System.out.println("Zone [" + zone.getName() + "] added.");
        }
    }

    // Edit a zone
    public void editZone(int code, String newName, Zonestatus status) {
        Zones z = findZone(code);

        if (z == null) {
            System.out.println("Zone not found.");
            return;
        }
            System.out.println("Zone [" + z.getName() + "] updated:");
            System.out.println("  Name   : " + z.getName() + " → " + newName);
            z.setName(newName);
            System.out.println("  Status : " + z.getStatus() + " → " + status);
            z.setStatus(status);
            saveZones();

    }



    //Deactivate a zone
    public void deactivateZone(int code){
        for (Zones z:zones){
            if(z.getUniquecode()==code){
                z.suspend();
                saveZones();
                return;
            }
        }
        System.out.println("Zone not found ");
    }

    public void deleteZone(int code) {
        Iterator<Zones> iterator = zones.iterator();
        while (iterator.hasNext()) {
            Zones zone = iterator.next();
            if (zone.getUniquecode() == code) {
                iterator.remove();
                saveZones();
                System.out.println("Zone [" + zone.getName() + "] deleted.");
                return;
            }
        }
        System.out.println("Zone not found.");
    }

    //search or find a zone
    public Zones findZone(int code) {
        for (Zones z : zones) {
            if (z.getUniquecode() == code) return z;
        }
        return null;
    }

    public List<Zones> getZones() {
        return new ArrayList<>(zones);
    }

    private void saveZones() {
        zoneStorage.saveZones(zones);
    }


    // Assign crop to CropZone
    public void assignCropToZone(int zoneCode, Crop crop) {
        Zones z = findZone(zoneCode);

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
    public void assignAnimalToZone(int zoneCode, Animal animal) {
        Zones z = findZone(zoneCode);

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
    public void recordProduction(int zoneCode, LocalDate date, double quantity) {
        Zones z = findZone(zoneCode);
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
    public void CropStatusReport(int codezone){
        Zones zone=findZone(codezone);

        if(zone==null){
            System.out.println("zone with code "+codezone+"does not exist ");
           return;
        }
        if (zone instanceof Cropzone){
            ((Cropzone) zone).generateCropStatusReport();
        }
    }

    //feeding schedules

    // Display feeding schedule for a specific zone by code
    public void displayFeedingSchedule(int zoneCode) {
        Zones z = findZone(zoneCode);

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
