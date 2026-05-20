package moduls;

import java.time.LocalDate;

public abstract class Zones  implements Monitorable{
    private int uniquecode;
    private String name;
    protected Zonestatus  Status ;

    public Zones(int code, String name) {
        this.uniquecode = code;
        this.name = name;
        this.Status = Zonestatus.ACTIVE;
    }

    public int getUniquecode() {return uniquecode;}
    public String getName() {
        return name;
    }
    public Zonestatus getStatus() {return Status;}
    public void setStatus(Zonestatus status) { Status = status;}
    public void setName(String name) {this.name = name;}


    @Override
    public boolean isActive() {
        return (this.Status==Zonestatus.ACTIVE);
    }

    @Override
    public void activate() {
        this.Status=Zonestatus.ACTIVE;
        //add the activation of sensors
        System.out.println("Zone [" + name + "]  ACTIVE");
    }

    @Override
    public void suspend() {
        this.Status=Zonestatus.SUSPENDED;
        //add the disactivation of sensors
        System.out.println("Zone [" + name + "] → SUSPENDED");
    }

    public abstract void display();


}
