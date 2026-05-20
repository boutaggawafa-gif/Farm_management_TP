package moduls;
 import java.time.LocalDate;
public class HealthEvent {
    private TypeEvent type;
    private String Description ;

    public HealthEvent(TypeEvent event, String description) {
        this.type=event;
        Description = description;
    }

   void displayHealthEvent(){
       System.out.println("Type of Event :"+ type);
       System.out.println("Description : "+Description);
   }

}
