package moduls;
import  java.time.LocalDate;
public interface Producible {
   abstract void  recordProduction(LocalDate date, double quantity);
   abstract void displayProduction();
    abstract void displayTotalProduction() ;
}