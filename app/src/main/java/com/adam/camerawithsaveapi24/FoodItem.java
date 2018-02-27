package com.adam.camerawithsaveapi24;

/**
 * Created by Adam on 25/02/2018.
 */

public class FoodItem {

    private String food_name;

    private double serving_weight; //grams
    private double calories;
    private double total_fat;
    private double saturated_fat;
    private double cholesterol;
    private double sodium;
    private double carbs;
    private double fiber;
    private double sugars;
    private double protein;
    private double potassium;


    public FoodItem(String fn, String serving_weight, String calories, String total_fat, String saturated_fat, String cholesterol, String sodium, String carbs, String fiber, String sugars, String protein, String potassium) {
        this.food_name = fn;
        this.serving_weight = parseDoubleSafely(serving_weight);
        this.calories = parseDoubleSafely(calories);
        this.total_fat = parseDoubleSafely(total_fat);
        this.saturated_fat = parseDoubleSafely(saturated_fat);
        this.cholesterol = parseDoubleSafely(cholesterol);
        this.sodium = parseDoubleSafely(sodium);
        this.carbs = parseDoubleSafely(carbs);
        this.fiber = parseDoubleSafely(fiber);
        this.sugars = parseDoubleSafely(sugars);
        this.protein = parseDoubleSafely(protein);
        this.potassium = parseDoubleSafely(potassium);
    }

    public static double parseDoubleSafely(String str) {
        double result = 0;
        try {
            result = Double.parseDouble(str);
        } catch (NullPointerException npe) {
        } catch (NumberFormatException nfe) {
        }
        return result;
    }

    public double getServing_weight() {
        return serving_weight;
    }

    public double getCalories() {
        return calories;
    }

    public double getTotal_fat() {
        return total_fat;
    }

    public double getSaturated_fat() {
        return saturated_fat;
    }

    public double getCholesterol() {
        return cholesterol;
    }

    public double getSodium() {
        return sodium;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFiber() {
        return fiber;
    }

    public double getSugars() {
        return sugars;
    }

    public double getProtein() {
        return protein;
    }

    public double getPotassium() {
        return potassium;
    }

    public String getFood_name() {

        return food_name;
    }

    @Override
    public String toString() {
        return
                "Food Item : " + this.food_name +
                        "Serving: " + this.serving_weight +
                        "Calories: " + this.calories +
                        "Total Fat: " + this.total_fat +
                        "Sat_Fat: " + this.saturated_fat +
                        "Cholesterol: " + this.cholesterol +
                        "Sodium: " + this.sodium +
                        "Carbs: " + this.carbs +
                        "Fiber: " + this.fiber +
                        "Sugar: " + this.sugars +
                        "Protein: " + this.protein +
                        "Potas: " + this.potassium;
    }
}
