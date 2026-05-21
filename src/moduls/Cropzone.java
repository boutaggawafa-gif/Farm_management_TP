package moduls;
import Sensors.EnvironmentalSensor;

import java.time.LocalDate;
import java.util.*;
public class Cropzone extends Zones implements Producible {

    private List<Crop> crops = new ArrayList<>();
    private List<EnvironmentalSensor> environmentalSensors = new ArrayList<>();
    private List<ProductionInfo> productionInfos = new ArrayList<>();
    private double totalp;

    public Cropzone(int zoneCode, String zoneName) {
        super(zoneCode, zoneName);
        this.totalp=0;
    }

    @Override
    public void displayTotalProduction() {
        System.out.println("Zone: " + getName()
                + " | Total Yield : " + totalp + " tons");
    }


    public void addCrop(Crop crop) {
        if (crop != null) {
            crops.add(crop);
        }
    }

    public int getCropsCount() {
        return crops.size();
    }


    @Override
    public void recordProduction(LocalDate date, double quantity) {
        ProductionInfo p = new ProductionInfo(date, quantity, ProductionType.CROP_YIELD, "tons");
        this.totalp += quantity;
        productionInfos.add(p);
        System.out.println("Production recorded successfully for CropZone ["
                + getName() + "]");
    }

    @Override
    public void displayProduction() {
        System.out.println("=== Crop Production Records - Zone: " + getName() + " ===");
        if (productionInfos.isEmpty()) {
            System.out.println("  No production records yet.");
            return;
        }
        for (ProductionInfo p : productionInfos) {
            p.display();
        }
        System.out.println("  Total Yield : " + totalp + " tons");
    }

    @Override
    public void display() {
        System.out.println("=== CropZone ===");
        System.out.println("  Code  : " + getUniquecode());
        System.out.println("  Name  : " + getName());
        System.out.println("  Status: " + Status);
        System.out.println("  Crops :");
        if (crops.isEmpty()) {
            System.out.println("    No crops yet.");
        } else {
            for (Crop c : crops) {
                c.display();
            }
        }
    }

  //register a crop is the same as add a crop
    public void displayGrowthAllCrop(){
        Iterator<Crop> it=crops.iterator();
        int i=1;
        while(it.hasNext()){
            System.out.println("--------------Crop "+i +"----------");
            Crop C =it.next();
            C.displayCuGrowth();
            System.out.println("------------------------------------");
        }
    }

    public void generateCropStatusReport() {
        System.out.println("========================================");
        System.out.println("       CROP STATUS REPORT               ");
        System.out.println("========================================");
        System.out.println("Zone Code  : " + getUniquecode());
        System.out.println("Zone Name  : " + getName());
        System.out.println("Zone Status: " + Status);
        System.out.println("Total Crops: " + crops.size());
        System.out.println("----------------------------------------");

        if (crops.isEmpty()) {
            System.out.println("  No crops registered in this zone.");
        } else {
            int i = 1;
            for (Crop c : crops) {
                System.out.println("Crop #" + i);
                c.generateStatusReport();
                i++;
            }
        }
        System.out.println("----------------------------------------");
    }
    public void addSensor(EnvironmentalSensor sensor) {
        environmentalSensors.add(sensor);
    }

    protected void suspendSensors() {
        for (EnvironmentalSensor s : environmentalSensors) s.suspend();
    }
    @Override
    protected void activateSensors() {
        for (EnvironmentalSensor s : environmentalSensors) s.activate();
    }
    }







