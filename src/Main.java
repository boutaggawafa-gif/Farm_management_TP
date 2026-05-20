import moduls.*;

import java.util.*;
import java.time.LocalDate;
public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

      /*  System.out.println("============================Manage farm zones and entities =======================");
        System.out.println("=======Q1========");
        Farm farm =new Farm();
        Cropzone cropzone=new Cropzone(123,"crop");
        LivestockZone livestockZone=new LivestockZone(111,"zone1","feedtype",14);
        farm.addZone(cropzone);
        farm.addZone(livestockZone);
        farm.displayOverview();


// Add
        Cropzone crop = new Cropzone(1, "North Field");
        farm.addZone(crop);

// Edit
        farm.editZone(1, "South Field");

// Deactivate
        farm.deactivateZone(1);
        farm.editZone(1,"supspended zone edit ");

        System.out.println("==========Q2=================");


// setup zones
        Cropzone crop2 = new Cropzone(156, "North Field");
        Ruminants ruminant = new Ruminants(2, "ruminant zone A", "hay", 3);
        farm.addZone(crop2);
        farm.addZone(ruminant);

// assign crop
        SoilRequirements soilReq=new SoilRequirements(3,10,20,30);
        Crop wheat = new Crop(Family.CEREALS,LocalDate.now(),GrowthStage.SOWING,"14/04/2025", soilReq);
        farm.assignCropToZone(156, wheat);
        crop2.display();

// assign animal
        Animal cow = new Animal(101, "Cow", 3,HealthStatus.HEALTHY, 500.0);
             farm.assignAnimalToZone(2, cow);
             ruminant.display();


        System.out.println("==================================manage crops ===============================");

                Cropzone zone1 = new Cropzone(101, "North Field");
                Cropzone zone2 = new Cropzone(102, "South Field");

                farm.addZone(zone1);
                farm.addZone(zone2);


                SoilRequirements soil1 = new SoilRequirements(6.0, 7.0, 40, 60);
                SoilRequirements soil2 = new SoilRequirements(5.5, 6.5, 50, 70);
                SoilRequirements soil3 = new SoilRequirements(6.5, 7.5, 35, 55);



                Crop tomato = new Crop(
                        Family.VEGETABLES,
                        LocalDate.of(2025, 4, 15),
                        GrowthStage.MATURITY,
                        "2025-07-15",
                        soil2
                );

                Crop apple = new Crop(
                        Family.FRUITS,
                        LocalDate.of(2025, 2, 10),
                        GrowthStage.SOWING,
                        "2025-09-01",
                        soil3
                );


                System.out.println("===== Assigning Crops =====");
                farm.assignCropToZone(101, wheat);
                farm.assignCropToZone(101, tomato);
                farm.assignCropToZone(102, apple);


                System.out.println("\n===== Update Growth Stage =====");
                wheat.UpdateGrowth(GrowthStage.MATURITY);


                System.out.println("\n===== Display Growth Stages (zone 101) =====");
                zone1.displayGrowthAllCrop();




                System.out.println("\n===== Farm Overview =====");
                farm.displayOverview();


                System.out.println("\n===== Crop Status Report - Zone 101 =====");
                farm.CropStatusReport(101);

                System.out.println("\n===== Crop Status Report - Zone 102 =====");
                farm.CropStatusReport(102);


                System.out.println("\n===== Test Suspended Zone =====");
                farm.deactivateZone(101);
                farm.assignCropToZone(101, apple);


                System.out.println("\n===== Test Invalid Zone Code =====");
                farm.CropStatusReport(999);


        System.out.println("============================Production record ==========================");
               cropzone.recordProduction(LocalDate.now(),125);
               System.out.println("=======test log health event ==========");
               HealthEvent H=new HealthEvent(TypeEvent.ILLNESS,"it is illness ");
               cow.addHealthEvent(H,"2006-04-30");
                cow.addHealthEvent(H,"2005-04-30");
               cow.dispalyAllEvents();


        System.out.println("====== feeding programe ====================");
        Feedingprogramme F =new Feedingprogramme("type feed ",23);
        livestockZone.setFeedingprogramme(F);

        livestockZone.getFeedingprogramme().display();

            }*/

        Farm farm = new Farm();

        // ============================================================
        // Q1 — Manage Farm Zones and Entities
        // ============================================================
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println(  "║        Q1 — Manage Farm Zones & Entities     ║");
        System.out.println(  "╚══════════════════════════════════════════════╝");

        // --- Add zones ---
        Cropzone cropzone      = new Cropzone(123, "Crop Zone");
        LivestockZone livestock = new LivestockZone(111, "zone1", "feedtype", 14);
        farm.addZone(cropzone);
        farm.addZone(livestock);

        // --- Edit zone ---
        Cropzone crop = new Cropzone(1, "North Field");
        farm.addZone(crop);
        farm.editZone(1, "South Field",Zonestatus.ACTIVE);

        // --- Deactivate zone ---
        farm.deactivateZone(1);
        farm.editZone(1, "Suspended Zone Edit",Zonestatus.SUSPENDED);  // test: edit suspended zone

        // --- Assign crop to zone ---
        Cropzone crop2 = new Cropzone(156, "North Field");
        farm.addZone(crop2);
        SoilRequirements soilReq = new SoilRequirements(3, 10, 20, 30);
        Crop wheat = new Crop(Family.CEREALS, LocalDate.now(), GrowthStage.SOWING, "14/04/2025", soilReq);
        farm.assignCropToZone(156, wheat);
        crop2.display();

        // --- Assign animal to zone ---
        Ruminants ruminant = new Ruminants(2, "Ruminant Zone A", "hay", 3);
        farm.addZone(ruminant);
        Animal cow = new Animal(101, "Cow", 3, HealthStatus.HEALTHY, 500.0);
        farm.assignAnimalToZone(2, cow);
        ruminant.display();

        // --- Display overview ---
        System.out.println("\n--- Farm Overview ---");
        farm.displayOverview();

        // --- Record production ---
        cropzone.recordProduction(LocalDate.now(), 125);
        cropzone.displayProduction();
        cropzone.displayTotalProduction();
        // production in farm
        farm.displayAllProductionsSummary();



        // ============================================================
        // Q2 — Manage Crops
        // ============================================================
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println(  "║             Q2 — Manage Crops                ║");
        System.out.println(  "╚══════════════════════════════════════════════╝");

        Cropzone zone1 = new Cropzone(101, "North Field");
        Cropzone zone2 = new Cropzone(102, "South Field");
        farm.addZone(zone1);
        farm.addZone(zone2);

        // --- Register crops ---
        SoilRequirements soil2 = new SoilRequirements(5.5, 6.5, 50, 70);
        SoilRequirements soil3 = new SoilRequirements(6.5, 7.5, 35, 55);

        Crop tomato = new Crop(Family.VEGETABLES, LocalDate.of(2025,4,15),
                GrowthStage.MATURITY, "2025-07-15", soil2);
        Crop apple  = new Crop(Family.FRUITS, LocalDate.of(2025,2,10),
                GrowthStage.SOWING, "2025-09-01", soil3);

        System.out.println("\n--- Assigning Crops ---");
        farm.assignCropToZone(101, wheat);
        farm.assignCropToZone(101, tomato);
        farm.assignCropToZone(102, apple);
        farm.assignCropToZone(10, apple);//invalid doesn't exist
        farm.assignCropToZone(2, wheat);//ruminant not crop zone

        // ------- Update growth stage ------
        System.out.println("\n--- Update Growth Stage ---");
        wheat.UpdateGrowth(GrowthStage.MATURITY);

        // --- Display growth stages ---
        System.out.println("\n--- Display Growth Stages (zone 101) ---");
        zone1.displayGrowthAllCrop();

        // --- Crop status report ---
        System.out.println("\n--- Crop Status Report - Zone 101 ---");
        farm.CropStatusReport(101);
        System.out.println("\n--- Crop Status Report - Zone 102 ---");
        farm.CropStatusReport(102);
        //test for one crop
        tomato.generateStatusReport();

        // --- Test suspended zone ---
        System.out.println("\n--- Test Suspended Zone ---");
        farm.deactivateZone(101);
        farm.assignCropToZone(101, apple);

        // --- Test invalid zone ---
        System.out.println("\n--- Test Invalid Zone Code ---");
        farm.CropStatusReport(999);


        // ============================================================
        // Q3 — Manage Animals
        // ============================================================
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println(  "║           Q3 — Manage Animals                ║");
        System.out.println(  "╚══════════════════════════════════════════════╝");

        // --- Register animal ---
        System.out.println("\n--- Register Animal ---");
        Animal sheep = new Animal(102, "Sheep", 2, HealthStatus.HEALTHY, 80.0);
        farm.assignAnimalToZone(2, sheep);


        // --- Log health events ---
        System.out.println("\n--- Log Health Events ---");
        HealthEvent illness = new HealthEvent(TypeEvent.ILLNESS, "Respiratory infection");
        cow.addHealthEvent(illness, "2006-04-30");
        cow.addHealthEvent(illness, "2005-04-30");
        cow.dispalyAllEvents();

        // --- Define and display feeding schedule per zone ---
        System.out.println("\n---- Feeding Schedule - Livestock Zone ----");

        // Create a LivestockZone
        LivestockZone zoneL = new LivestockZone(1, "Cattle Zone", "Hay", 5);

        //  Display zone before adding anything
        System.out.println("======= Before Setup =======");
        zoneL.displayFeedingSchedules();

        // Define feeding schedule
        System.out.println("\n======= Defining Schedule =======");
        zoneL.defineFeedingSchedule(List.of("07:00", "12:00", "18:00"));

        //  Display feeding schedule after defining it
        System.out.println("\n======= Feeding Schedule =======");
        zoneL.displayFeedingSchedules();

        System.out.println("\n=== Test: Specific Zone Schedule ===");
        farm.displayFeedingSchedule(1); // Cattle Zone
        farm.displayFeedingSchedule(2); // Fish Zone
        farm.displayFeedingSchedule(99); //

        System.out.println("\n=== Test: All Feeding Schedules ===");
        farm.displayAllFeedingSchedules();
    }
}



