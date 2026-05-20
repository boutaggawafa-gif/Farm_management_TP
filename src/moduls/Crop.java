package moduls;
import org.w3c.dom.ls.LSOutput;

import java.time.LocalDate;

public class Crop { ;
    private Family family;
    private LocalDate plantingDate;
    private String harvestDate;
    private GrowthStage growthStage;
    private SoilRequirements soilRequirements;

    public Crop(Family family ,LocalDate plantingDate, GrowthStage growthStage, String harvestDate,SoilRequirements soilRequirementsC) {
        this.plantingDate = plantingDate;
        this.family=family;
        this.growthStage = growthStage;
        this.harvestDate = harvestDate;
        this.soilRequirements=soilRequirementsC;
    }

    public void display() {
        System.out.println("  Crop:");
        System.out.println("    Family       : " + family);
        System.out.println("    Planting Date: " + plantingDate);
        System.out.println("    Harvest Date : " + harvestDate);
        System.out.println("    Growth Stage : " + growthStage);
    }

    public void displayCuGrowth(){
        System.out.println("Current Growth stage :"+this.growthStage);
    }

    public void UpdateGrowth(GrowthStage growthStage){
        System.out.println("current Growth stage :"+this.growthStage);
        this.growthStage=growthStage;
        System.out.println("new Growth stage :"+this.growthStage);
    }

    public void generateStatusReport() {
        System.out.println("  ---- Crop Report ----");
        System.out.println("  Family       : " + family);
        System.out.println("  Planting Date: " + plantingDate);
        System.out.println("  Harvest Date : " + harvestDate);
        System.out.println("  Growth Stage : " + growthStage);
        System.out.println("  Soil Requirements: " + soilRequirements);
    }

}
