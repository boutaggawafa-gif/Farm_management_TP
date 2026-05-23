package moduls;

import Exception.ZoneIsNullException;
import Exception.ZoneSuspendedException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Farm {
    private List<Zones> zones = new ArrayList<>();
    private ZoneStorage zoneStorage = new ZoneStorage();

    public Farm() {
        zones.addAll(zoneStorage.loadZones());
    }

    public void addZone(Zones zone) {
        try {
            if (zone == null) {
                throw new ZoneIsNullException("Cannot add a null zone.");
            }
            if (findZone(zone.getUniquecode()) != null) {
                System.out.println("Zone with code " + zone.getUniquecode() + " already exists.");
                return;
            }
            zones.add(zone);
            saveZones();
            System.out.println("Zone [" + zone.getName() + "] added.");
        } catch (ZoneIsNullException e) {
            System.out.println(e.getMessage());
        }
    }

    public void editZone(int code, String newName, Zonestatus status) {
        try {
            Zones z = requireZone(code);
            System.out.println("Zone [" + z.getName() + "] updated:");
            System.out.println("  Name   : " + z.getName() + " -> " + newName);
            z.setName(newName);
            System.out.println("  Status : " + z.getStatus() + " -> " + status);
            z.setStatus(status);
            saveZones();
        } catch (ZoneIsNullException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deactivateZone(int code) {
        try {
            Zones z = requireZone(code);
            z.suspend();
            saveZones();
        } catch (ZoneIsNullException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteZone(int code) {
        try {
            requireZone(code);
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
        } catch (ZoneIsNullException e) {
            System.out.println(e.getMessage());
        }
    }

    public Zones findZone(int code) {
        for (Zones z : zones) {
            if (z.getUniquecode() == code) {
                return z;
            }
        }
        return null;
    }

    private Zones requireZone(int code) throws ZoneIsNullException {
        Zones zone = findZone(code);
        if (zone == null) {
            throw new ZoneIsNullException("Zone with code " + code + " not found.");
        }
        return zone;
    }

    private void requireActiveZone(Zones zone) throws ZoneSuspendedException {
        if (!zone.isActive()) {
            throw new ZoneSuspendedException("Zone [" + zone.getName() + "] is SUSPENDED.");
        }
    }

    public List<Zones> getZones() {
        return new ArrayList<>(zones);
    }

    private void saveZones() {
        zoneStorage.saveZones(zones);
    }

    public void assignCropToZone(int zoneCode, Crop crop) {
        try {
            Zones z = requireZone(zoneCode);
            requireActiveZone(z);

            if (!(z instanceof Cropzone)) {
                System.out.println("Zone [" + z.getName() + "] is not a CropZone.");
                return;
            }

            ((Cropzone) z).addCrop(crop);
            System.out.println("Crop assigned to zone [" + z.getName() + "]");
        } catch (ZoneIsNullException | ZoneSuspendedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void assignAnimalToZone(int zoneCode, Animal animal) {
        try {
            Zones z = requireZone(zoneCode);
            requireActiveZone(z);

            if (!(z instanceof LivestockZone)) {
                System.out.println("Zone [" + z.getName() + "] is not a LivestockZone.");
                return;
            }

            ((LivestockZone) z).addAnimal(animal);
            System.out.println("Animal assigned to zone [" + z.getName() + "]");
        } catch (ZoneIsNullException | ZoneSuspendedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayOverview() {
        System.out.println("========== FARM OVERVIEW ==========");
        System.out.println("Total zones: " + zones.size());
        System.out.println("===================================");

        for (Zones z : zones) {
            System.out.println("Zone  : " + z.getName());
            System.out.println("Code  : " + z.getUniquecode());
            System.out.println("Status: " + z.getStatus());

            if (z instanceof Cropzone c) {
                System.out.println("Crops : " + c.getCropsCount());
            } else if (z instanceof LivestockZone l) {
                System.out.println("Animals: " + l.getAnimalscount());
            } else if (z instanceof AquacultureZone a) {
                System.out.println("Species: " + a.getNumberOfAnimals());
            }

            System.out.println("-----------------------------------");
        }
    }

    public void recordProduction(int zoneCode, LocalDate date, double quantity) {
        try {
            Zones z = requireZone(zoneCode);
            requireActiveZone(z);

            if (!(z instanceof Producible)) {
                System.out.println("Zone does not support production.");
                return;
            }

            ((Producible) z).recordProduction(date, quantity);
        } catch (ZoneIsNullException | ZoneSuspendedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayAllProductionsSummary() {
        System.out.println("======= PRODUCTION SUMMARY =======");
        for (Zones z : zones) {
            if (z instanceof Producible) {
                ((Producible) z).displayTotalProduction();
                System.out.println("---------------------------------");
            }
        }
    }

    public void CropStatusReport(int codezone) {
        try {
            Zones zone = requireZone(codezone);
            if (zone instanceof Cropzone) {
                ((Cropzone) zone).generateCropStatusReport();
            }
        } catch (ZoneIsNullException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayFeedingSchedule(int zoneCode) {
        try {
            Zones z = requireZone(zoneCode);

            if (z instanceof LivestockZone) {
                ((LivestockZone) z).displayFeedingSchedules();
            } else if (z instanceof AquacultureZone) {
                ((AquacultureZone) z).displayFeedingSchedules();
            } else {
                System.out.println("Zone [" + z.getName() + "] does not have a feeding schedule.");
            }
        } catch (ZoneIsNullException e) {
            System.out.println(e.getMessage());
        }
    }

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
