package moduls;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Ruminants extends LivestockZone implements Producible{

    private TreeMap<LocalDate,ProductionInfo> productionRecords=new TreeMap<>();
    private double totalP;


    public Ruminants(int code, String nam, String feedType, int quantitiesPerMeal) {
        super(code, nam, feedType, quantitiesPerMeal);
        this.totalP=0;
    }

    @Override
    public void displayTotalProduction() {
        System.out.println("Zone: " + getName()
                + " | Total Milk : " + totalP + " liters");
    }

    @Override
    public void recordProduction(LocalDate date, double quantity) {
        ProductionInfo p = new ProductionInfo(quantity, ProductionType.MILK_YIELD, "liters");
        this.totalP += quantity;
        productionRecords.put(date,p);
        System.out.println("Production recorded successfully for Ruminants Zone [" + getName() + "]");
    }

    @Override
    public void displayProduction() {
        System.out.println("=== Milk Yield Records - Zone: " + getName() + " ===");
        if (productionRecords.isEmpty()) {
            System.out.println("  No production records yet.");
            return;
        }
        for(Map.Entry<LocalDate,ProductionInfo> entry :productionRecords.entrySet() ){
            System.out.println(entry.getKey());
            entry.getValue().display();
        }
        System.out.println("  Total Milk Yield : " + totalP + " liters");
    }



}
