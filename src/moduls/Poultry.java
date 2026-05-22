package moduls;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Poultry extends LivestockZone implements Producible{

    private TreeMap<LocalDate,ProductionInfo> productionRecords=new TreeMap<>();
    private double totalP;




    public Poultry(int code, String name, String feedType, int quantitiesPerMeal) {
        super(code, name, feedType, quantitiesPerMeal);
        this.totalP =0 ;
    }



    public void recordProduction(LocalDate date, double quantity) {
        ProductionInfo P=new ProductionInfo(quantity,ProductionType.EGG,"eggs");
        this.totalP +=quantity;
     productionRecords.put(date,P);
    }

    @Override
    public void displayProduction() {
        System.out.println("=== Egg Count Records - Zone: " + getName() + " ===");
        if (productionRecords.isEmpty()) {
            System.out.println("  No production records yet.");
            return;
        }
        for(Map.Entry<LocalDate,ProductionInfo> entry :productionRecords.entrySet() ){
            System.out.println(entry.getKey());
            entry.getValue().display();
        }
        System.out.println("  Total Eggs : " + totalP + " eggs");
    }

    @Override
    public void displayTotalProduction() {
        System.out.println("Zone: " + getName()
                + " | Total Eggs : " + totalP + " eggs");
    }


}
