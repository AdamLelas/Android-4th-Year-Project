package com.adam.camerawithsaveapi24.classes;

import static com.adam.camerawithsaveapi24.tools.Utility.*;
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
    private double sugar;
    private double protein;
    private double potassium;
    private double servings;
    private double iron;
    private double calcium;
    private double vitamin_a;
    private double vitamin_b1;
    private double vitamin_b2;
    private double vitamin_b3;
    private double vitamin_b5;
    private double vitamin_b6;
    private double vitamin_b9;
    private double vitamin_b12;
    private double vitamin_c;
    private double vitamin_d;
    private double vitamin_e;
    private double vitamin_k;


    public double getVitamin_b6() {
        return vitamin_b6;
    }

    public void setVitamin_b6(double vitamin_b6) {
        this.vitamin_b6 = vitamin_b6;
    }

    public double getIron() {
        return iron;
    }

    public void setIron(double iron) {
        this.iron = iron;
    }

    public double getCalcium() {
        return calcium;
    }

    public void setCalcium(double calcium) {
        this.calcium = calcium;
    }

    public double getVitamin_a() {
        return vitamin_a;
    }

    public void setVitamin_a(double vitamin_a) {
        this.vitamin_a = vitamin_a;
    }

    public double getVitamin_b1() {
        return vitamin_b1;
    }

    public void setVitamin_b1(double vitamin_b1) {
        this.vitamin_b1 = vitamin_b1;
    }

    public double getVitamin_b2() {
        return vitamin_b2;
    }

    public void setVitamin_b2(double vitamin_b2) {
        this.vitamin_b2 = vitamin_b2;
    }

    public double getVitamin_b3() {
        return vitamin_b3;
    }

    public void setVitamin_b3(double vitamin_b3) {
        this.vitamin_b3 = vitamin_b3;
    }

    public double getVitamin_b5() {
        return vitamin_b5;
    }

    public void setVitamin_b5(double vitamin_b5) {
        this.vitamin_b5 = vitamin_b5;
    }

    public double getVitamin_b9() {
        return vitamin_b9;
    }

    public void setVitamin_b9(double vitamin_b9) {
        this.vitamin_b9 = vitamin_b9;
    }

    public double getVitamin_b12() {
        return vitamin_b12;
    }

    public void setVitamin_b12(double vitamin_b12) {
        this.vitamin_b12 = vitamin_b12;
    }

    public double getVitamin_c() {
        return vitamin_c;
    }

    public void setVitamin_c(double vitamin_c) {
        this.vitamin_c = vitamin_c;
    }

    public double getVitamin_d() {
        return vitamin_d;
    }

    public void setVitamin_d(double vitamin_d) {
        this.vitamin_d = vitamin_d;
    }

    public double getVitamin_e() {
        return vitamin_e;
    }

    public void setVitamin_e(double vitamin_e) {
        this.vitamin_e = vitamin_e;
    }

    public double getVitamin_k() {
        return vitamin_k;
    }

    public void setVitamin_k(double vitamin_k) {
        this.vitamin_k = vitamin_k;
    }

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

    public void setSugar(double sugar) {
        this.sugar = sugar;
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

    public FoodItem() {
    }

    public FoodItem(String fn, String serving_weight, String calories, String total_fat,
                    String saturated_fat, String cholesterol, String sodium,
                    String carbs, String fiber, String sugar, String protein, String potassium) {
        this.food_name = fn;
        this.serving_weight_grams = parseDoubleSafely(serving_weight);
        this.calories = parseDoubleSafely(calories);
        this.total_fat = parseDoubleSafely(total_fat);
        this.saturated_fat = parseDoubleSafely(saturated_fat);
        this.cholesterol = parseDoubleSafely(cholesterol);
        this.sodium = parseDoubleSafely(sodium);
        this.carbs = parseDoubleSafely(carbs);
        this.fiber = parseDoubleSafely(fiber);
        this.sugar = parseDoubleSafely(sugar);
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
        this.sugar = parseDoubleSafely(sugars);
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

    public double getSugar() {
        return sugar;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodItem foodItem = (FoodItem) o;

        if (Double.compare(foodItem.getServing_weight_grams(), getServing_weight_grams()) != 0)
            return false;
        if (Double.compare(foodItem.getCalories(), getCalories()) != 0) return false;
        if (Double.compare(foodItem.getTotal_fat(), getTotal_fat()) != 0) return false;
        if (Double.compare(foodItem.getSaturated_fat(), getSaturated_fat()) != 0) return false;
        if (Double.compare(foodItem.getCholesterol(), getCholesterol()) != 0) return false;
        if (Double.compare(foodItem.getSodium(), getSodium()) != 0) return false;
        if (Double.compare(foodItem.getCarbs(), getCarbs()) != 0) return false;
        if (Double.compare(foodItem.getFiber(), getFiber()) != 0) return false;
        if (Double.compare(foodItem.getSugar(), getSugar()) != 0) return false;
        if (Double.compare(foodItem.getProtein(), getProtein()) != 0) return false;
        if (Double.compare(foodItem.getPotassium(), getPotassium()) != 0) return false;
        if (Double.compare(foodItem.getServings(), getServings()) != 0) return false;
        if (Double.compare(foodItem.getIron(), getIron()) != 0) return false;
        if (Double.compare(foodItem.getCalcium(), getCalcium()) != 0) return false;
        if (Double.compare(foodItem.getVitamin_a(), getVitamin_a()) != 0) return false;
        if (Double.compare(foodItem.getVitamin_b1(), getVitamin_b1()) != 0) return false;
        if (Double.compare(foodItem.getVitamin_b2(), getVitamin_b2()) != 0) return false;
        if (Double.compare(foodItem.getVitamin_b3(), getVitamin_b3()) != 0) return false;
        if (Double.compare(foodItem.getVitamin_b5(), getVitamin_b5()) != 0) return false;
        if (Double.compare(foodItem.getVitamin_b6(), getVitamin_b6()) != 0) return false;
        if (Double.compare(foodItem.getVitamin_b9(), getVitamin_b9()) != 0) return false;
        if (Double.compare(foodItem.getVitamin_b12(), getVitamin_b12()) != 0) return false;
        if (Double.compare(foodItem.getVitamin_c(), getVitamin_c()) != 0) return false;
        if (Double.compare(foodItem.getVitamin_d(), getVitamin_d()) != 0) return false;
        if (Double.compare(foodItem.getVitamin_e(), getVitamin_e()) != 0) return false;
        if (Double.compare(foodItem.getVitamin_k(), getVitamin_k()) != 0) return false;

        return getFood_name() != null ? getFood_name().equals(foodItem.getFood_name()) : foodItem.getFood_name() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getFood_name() != null ? getFood_name().hashCode() : 0;
        temp = Double.doubleToLongBits(getServing_weight_grams());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getCalories());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getTotal_fat());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getSaturated_fat());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getCholesterol());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getSodium());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getCarbs());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getFiber());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getSugar());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getProtein());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getPotassium());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getServings());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getIron());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getCalcium());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamin_a());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamin_b1());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamin_b2());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamin_b3());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamin_b5());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamin_b6());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamin_b9());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamin_b12());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamin_c());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamin_d());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamin_e());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVitamin_k());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
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
                        " Sugar: " + this.sugar +
                        " Protein: " + this.protein +
                        " Potas: " + this.potassium;
    }


}
