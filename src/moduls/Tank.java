package moduls;
import java.util.*;

public class Tank {
    private List<AquacultureSpecies> aquacultureSpecies = new ArrayList<>();

    public void addSpecies(AquacultureSpecies species) {
        aquacultureSpecies.add(species);
    }

    public List<AquacultureSpecies> getAquacultureSpecies() {
        return aquacultureSpecies;
    }

    public void display() {
        System.out.println("  Tank:");
        if (aquacultureSpecies.isEmpty()) {
            System.out.println("    No species yet.");
        } else {
            System.out.println("    Aquaculture Species:");
            for( AquacultureSpecies s:aquacultureSpecies){
                s.display();
            }
            }
        }
    }

