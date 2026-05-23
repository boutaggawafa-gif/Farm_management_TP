package moduls;

import java.util.ArrayList;
import java.util.List;

public class Feedingprogramme {

    private String feedType;
    private int QuantitiesPerMeal;
    private List<String> schedule = new ArrayList<>();

    public Feedingprogramme(String feedType, int quantitiesPerMeal) {
        this.feedType = feedType;
        QuantitiesPerMeal = quantitiesPerMeal;
    }

    public String getFeedType() {
        return feedType;
    }

    public int getQuantitiesPerMeal() {
        return QuantitiesPerMeal;
    }

    public void addMealTime(String time) {
        if (time != null && !time.isEmpty()) {
            schedule.add(time);
            System.out.println("Meal time " + time + " added successfully.");
        }
    }

    public int getMealsPerDay() {
        return schedule.size();
    }

    public void display() {
        System.out.println("    Feed Type          : " + feedType);
        System.out.println("    Quantities Per Meal: " + QuantitiesPerMeal);
        System.out.println("    Meals Per Day      : " + getMealsPerDay());
        if (schedule.isEmpty()) {
            System.out.println("    Schedule           : No meals scheduled yet.");
        } else {
            System.out.println("    Schedule           :");
            for (int i = 0; i < schedule.size(); i++) {
                System.out.println("      Meal " + (i + 1) + " at " + schedule.get(i));
            }
        }
    }
}
