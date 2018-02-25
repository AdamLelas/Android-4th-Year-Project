package com.adam.camerawithsaveapi24;

/**
 * Created by Adam on 25/02/2018.
 */

public class FoodItem {

    private String food_name;
    private String serving_weight; //grams
    private String calories;
    private String total_fat;
    private String saturated_fat;
    private String cholesterol;
    private String sodium;
    private String carbs;
    private String fiber;
    private String sugars;
    private String protein;
    private String potassium;


    public FoodItem(String fn, String serving_weight, String calories, String total_fat, String saturated_fat, String cholesterol, String sodium, String carbs, String fiber, String sugars, String protein, String potassium) {
        this.food_name = fn;
        this.serving_weight = serving_weight;
        this.calories = calories;
        this.total_fat = total_fat;
        this.saturated_fat = saturated_fat;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.carbs = carbs;
        this.fiber = fiber;
        this.sugars = sugars;
        this.protein = protein;
        this.potassium = potassium;
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
