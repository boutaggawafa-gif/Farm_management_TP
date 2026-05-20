package moduls;
import java.time.LocalDate;
public class ProductionInfo {
    private LocalDate date;
    private ProductionType type;
    private double quantity;
    private String unit;

    public ProductionInfo(LocalDate date,double quantity,ProductionType type,String unit) {
        this.date = date;
        this.quantity = quantity;
        this.type = type;
        this.unit = unit;
    }

    public void display() {
        System.out.println("  [" + date + "] " + type + " : " + quantity + " " + unit);
    }


}
