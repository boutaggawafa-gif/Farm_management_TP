package moduls;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Poultry extends LivestockZone implements Producible{

    private List<ProductionInfo> productionInfos =new ArrayList<>();
    private double totalP;




    public Poultry(int code, String name, String feedType, int quantitiesPerMeal) {
        super(code, name, feedType, quantitiesPerMeal);
        this.totalP =0 ;
    }



    public void recordProduction(LocalDate date, double quantity) {
        ProductionInfo P=new ProductionInfo(date,quantity,ProductionType.EGG,"eggs");
        this.totalP +=quantity;
       productionInfos.add(P);
    }

    @Override
    public void displayProduction() {
        System.out.println("=== Egg Count Records - Zone: " + getName() + " ===");
        if (productionInfos.isEmpty()) {
            System.out.println("  No production records yet.");
            return;
        }
        for (ProductionInfo p : productionInfos) {
            p.display();
        }
        System.out.println("  Total Eggs : " + totalP + " eggs");
    }

    @Override
    public void displayTotalProduction() {
        System.out.println("Zone: " + getName()
                + " | Total Eggs : " + totalP + " eggs");
    }


}
