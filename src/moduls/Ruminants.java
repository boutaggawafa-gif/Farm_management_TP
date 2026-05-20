package moduls;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Ruminants extends LivestockZone implements Producible{

    private List<ProductionInfo> productionInfos =new ArrayList<>();
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
        ProductionInfo p = new ProductionInfo(
                date, quantity, ProductionType.MILK_YIELD, "liters"
        );
        this.totalP += quantity;
        productionInfos.add(p);
        System.out.println("Production recorded successfully for Ruminants Zone ["
                + getName() + "]");
    }

    @Override
    public void displayProduction() {
        System.out.println("=== Milk Yield Records - Zone: " + getName() + " ===");
        if (productionInfos.isEmpty()) {
            System.out.println("  No production records yet.");
            return;
        }
        for (ProductionInfo p : productionInfos) {
            p.display();
        }
        System.out.println("  Total Milk Yield : " + totalP + " liters");
    }



}
