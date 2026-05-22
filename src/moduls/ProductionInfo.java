package moduls;
import java.time.LocalDate;
public class ProductionInfo {
    private ProductionType type;
    private double quantity;
    private String unit;

    public ProductionInfo(double quantity,ProductionType type,String unit) {
        this.quantity = quantity;
        this.type = type;
        this.unit = unit;
    }

    public void display() {
        System.out.println( type + " : " + quantity + " " + unit);
    }

}
