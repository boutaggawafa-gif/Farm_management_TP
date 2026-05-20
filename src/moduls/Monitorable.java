package moduls;

public interface Monitorable {
  //activate zone and sensors
    void activate();

    //disactivate  zone and sensors
    void suspend();

    //return if the zone is active or no
    boolean isActive();
}
