package com.adam.camerawithsaveapi24.classes;

import static com.adam.camerawithsaveapi24.tools.Utility.*;


/**
 * Created by Adam on 25/02/2018.
 */

public class FoodItem {

    private String food_name;
    private double serving_weight_grams; //grams
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
    private double servings;

    public void setServing_weight_grams(double serving_weight_grams) {
        this.serving_weight_grams = serving_weight_grams;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setTotal_fat(double total_fat) {
        this.total_fat = total_fat;
    }

    public void setSaturated_fat(double saturated_fat) {
        this.saturated_fat = saturated_fat;
    }

    public void setCholesterol(double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public void setSugars(double sugars) {
        this.sugars = sugars;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public void setPotassium(double potassium) {
        this.potassium = potassium;
    }

    public void setServings(double servings) {
        this.servings = servings;
    }

    public void setFood_name(String food_name) {

        this.food_name = food_name;
    }

    public FoodItem(){}

    public FoodItem(String fn, String serving_weight, String calories, String total_fat, String saturated_fat, String cholesterol, String sodium, String carbs, String fiber, String sugars, String protein, String potassium) {
        this.food_name = fn;
        this.serving_weight_grams = parseDoubleSafely(serving_weight);
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

    public FoodItem(String fn, String serving_weight, String calories, String total_fat,
                    String saturated_fat, String cholesterol, String sodium, String carbs,
                    String fiber, String sugars, String protein, String potassium, String servAmt) {
        this.food_name = fn;
        this.serving_weight_grams = parseDoubleSafely(serving_weight);
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
        this.servings = parseDoubleSafely(servAmt);
    }




    public double getServing_weight_grams() {
        return serving_weight_grams;
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

    public double getServings() {
        return servings;
    }

    @Override
    public String toString() {
        return
                "Food Item : " + this.food_name +
                        "Num Servings: " + this.servings +
                        " Serving_weight: " + this.serving_weight_grams +
                        " Calories: " + this.calories +
                        " Total Fat: " + this.total_fat +
                        " Sat_Fat: " + this.saturated_fat +
                        " Cholesterol: " + this.cholesterol +
                        " Sodium: " + this.sodium +
                        " Carbs: " + this.carbs +
                        " Fiber: " + this.fiber +
                        " Sugar: " + this.sugars +
                        " Protein: " + this.protein +
                        " Potas: " + this.potassium;
    }


}
