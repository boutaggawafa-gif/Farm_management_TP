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
        Zonestatus previous = this.Status;
        this.Status = Zonestatus.ACTIVE;
        activateSensors();
        System.out.println("----------------------------------");
        System.out.println("  Zone Activated");
        System.out.println("  Name   : " + name);
        System.out.println("  Code   : " + getUniquecode());
        System.out.println("  Status : " + previous + " → " + this.Status);
        System.out.println("----------------------------------");
    }

    @Override
    public void suspend() {
        Zonestatus previous = this.Status;
        this.Status = Zonestatus.SUSPENDED;
        suspendSensors();
        System.out.println("----------------------------------");
        System.out.println("  Zone Suspended");
        System.out.println("  Name   : " + name);
        System.out.println("  Code   : " + getUniquecode());
        System.out.println("  Status : " + previous + " → " + this.Status);
        System.out.println("----------------------------------");
    }
    protected abstract void suspendSensors();
    protected abstract void activateSensors();
    public abstract void display();


}
