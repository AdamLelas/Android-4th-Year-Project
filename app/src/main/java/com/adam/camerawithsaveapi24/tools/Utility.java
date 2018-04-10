package com.adam.camerawithsaveapi24.tools;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.Spinner;

import com.adam.camerawithsaveapi24.classes.FoodItem;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class Utility {

    /**
     * used to add extras to items
     * Important: item1 should always be the Main food and item2 should always be the extras
     * example: adding sugar and/or milk to tea or coffee
     */
    public static FoodItem combineFoodItems(FoodItem item1, FoodItem item2) {
        FoodItem newFood = new FoodItem();
        newFood.setFood_name(item1.getFood_name() + ", with " + item2.getFood_name());
        newFood.setCalcium(item1.getCalcium() + (item2.getCalcium() * item2.getServings()));
        newFood.setCalories(item1.getCalories() + (item2.getCalories() * item2.getServings()));
        newFood.setCarbs(item1.getCarbs() + (item2.getCarbs() * item2.getServings()));
        newFood.setCholesterol(item1.getCholesterol() + (item2.getCholesterol() * item2.getServings()));
        newFood.setTotal_fat(item1.getTotal_fat() + (item2.getTotal_fat() * item2.getServings()));
        newFood.setSaturated_fat(item1.getSaturated_fat() + (item2.getSaturated_fat() * item2.getServings()));
        newFood.setSodium(item1.getSodium() + (item2.getSodium() * item2.getServings()));
        newFood.setProtein(item1.getProtein() + (item2.getProtein() * item2.getServings()));
        newFood.setVitamin_a(item1.getVitamin_a() + (item2.getVitamin_a() * item2.getServings()));
        newFood.setVitamin_b1(item1.getVitamin_b1() + (item2.getVitamin_b1() * item2.getServings()));
        newFood.setVitamin_b2(item1.getVitamin_b2() + (item2.getVitamin_b2() * item2.getServings()));
        newFood.setVitamin_b3(item1.getVitamin_b3() + (item2.getVitamin_b3() * item2.getServings()));
        newFood.setVitamin_b5(item1.getVitamin_b5() + (item2.getVitamin_b5() * item2.getServings()));
        newFood.setVitamin_b6(item1.getVitamin_b6() + (item2.getVitamin_b6() * item2.getServings()));
        newFood.setVitamin_b9(item1.getVitamin_b9() + (item2.getVitamin_b9() * item2.getServings()));
        newFood.setVitamin_b12(item1.getVitamin_b12() + (item2.getVitamin_b12() * item2.getServings()));
        newFood.setVitamin_c(item1.getVitamin_c() + (item2.getVitamin_c() * item2.getServings()));
        newFood.setVitamin_d(item1.getVitamin_d() + (item2.getVitamin_d() * item2.getServings()));
        newFood.setVitamin_e(item1.getVitamin_e() + (item2.getVitamin_e() * item2.getServings()));
        newFood.setVitamin_k(item1.getVitamin_k() + (item2.getVitamin_k() * item2.getServings()));
        newFood.setPotassium(item1.getPotassium() + (item2.getPotassium() * item2.getServings()));
        newFood.setFiber(item1.getFiber() + (item2.getFiber() * item2.getServings()));
        newFood.setSugar(item1.getSugar() + (item2.getSugar() * item2.getServings()));
        newFood.setIron(item1.getIron() + (item2.getIron() * item2.getServings()));
        newFood.setServing_weight_grams(item1.getServing_weight_grams() + (item2.getServing_weight_grams() * item2.getServings()));
        return newFood;
    }


    /**
     * divides all values by the serving weight to get the values for 1 gram of the food
     */
    public static FoodItem convertTo1Gram(FoodItem i) {
        FoodItem rv = new FoodItem();
        double divider = i.getServing_weight_grams();
        rv.setServings(1);
        rv.setFood_name(i.getFood_name());
        rv.setCalcium(i.getCalcium());
        rv.setCalcium((i.getCalcium()) / (divider));
        rv.setCalories(i.getCalories() / divider);
        rv.setCarbs(i.getCarbs() / divider);
        rv.setCholesterol(i.getCholesterol() / divider);
        rv.setTotal_fat(i.getTotal_fat() / divider);
        rv.setSaturated_fat(i.getSaturated_fat() / divider);
        rv.setSodium(i.getSodium() / divider);
        rv.setProtein(i.getProtein() / divider);
        rv.setVitamin_a(i.getVitamin_a() / divider);
        rv.setVitamin_b1(i.getVitamin_b1() / divider);
        rv.setVitamin_b2(i.getVitamin_b2() / divider);
        rv.setVitamin_b3(i.getVitamin_b3() / divider);
        rv.setVitamin_b5(i.getVitamin_b5() / divider);
        rv.setVitamin_b6(i.getVitamin_b6() / divider);
        rv.setVitamin_b9(i.getVitamin_b9() / divider);
        rv.setVitamin_b12(i.getVitamin_b12() / divider);
        rv.setVitamin_c(i.getVitamin_c() / divider);
        rv.setVitamin_d(i.getVitamin_d() / divider);
        rv.setVitamin_e(i.getVitamin_e() / divider);
        rv.setVitamin_k(i.getVitamin_k() / divider);
        rv.setPotassium(i.getPotassium() / divider);
        rv.setFiber(i.getFiber() / divider);
        rv.setSugar(i.getSugar() / divider);
        rv.setIron(i.getIron() / divider);
        rv.setServing_weight_grams(i.getServing_weight_grams() / divider);
        return rv;
    }

    public static FoodItem convertToXGrams(FoodItem i, double multiple) {
        System.out.println("CONVERT TO X GRAMS " + multiple);
        FoodItem rv = new FoodItem();
        rv.setServings(1);
        rv.setFood_name(i.getFood_name());
        rv.setCalcium(i.getCalcium());
        rv.setCalcium((i.getCalcium()) * (multiple));
        rv.setCalories(i.getCalories() * multiple);
        rv.setCarbs(i.getCarbs() * multiple);
        rv.setCholesterol(i.getCholesterol() * multiple);
        rv.setTotal_fat(i.getTotal_fat() * multiple);
        rv.setSaturated_fat(i.getSaturated_fat() * multiple);
        rv.setSodium(i.getSodium() * multiple);
        rv.setProtein(i.getProtein() * multiple);
        rv.setVitamin_a(i.getVitamin_a() * multiple);
        rv.setVitamin_b1(i.getVitamin_b1() * multiple);
        rv.setVitamin_b2(i.getVitamin_b2() * multiple);
        rv.setVitamin_b3(i.getVitamin_b3() * multiple);
        rv.setVitamin_b5(i.getVitamin_b5() * multiple);
        rv.setVitamin_b6(i.getVitamin_b6() * multiple);
        rv.setVitamin_b9(i.getVitamin_b9() * multiple);
        rv.setVitamin_b12(i.getVitamin_b12() * multiple);
        rv.setVitamin_c(i.getVitamin_c() * multiple);
        rv.setVitamin_d(i.getVitamin_d() * multiple);
        rv.setVitamin_e(i.getVitamin_e() * multiple);
        rv.setVitamin_k(i.getVitamin_k() * multiple);
        rv.setPotassium(i.getPotassium() * multiple);
        rv.setFiber(i.getFiber() * multiple);
        rv.setSugar(i.getSugar() * multiple);
        System.out.println("SUGAR SUGAR SUGAR SUGAR SUGAR SUGAR " + rv.getSugar());
        rv.setIron(i.getIron() * multiple);
        rv.setServing_weight_grams(i.getServing_weight_grams() * multiple);
        return rv;
    }

    /**
     * https://gist.github.com/laaptu/7867851
     **/
    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    /**
     * https://gist.github.com/laaptu/7867851
     **/
    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static String formatDouble(Double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(value);
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

    public static int parseIntSafely(String str) {
        int result = 0;
        try {
            result = Integer.parseInt(str);
        } catch (NullPointerException npe) {
        } catch (NumberFormatException nfe) {
        }
        return result;
    }

    public static double roundSafely(double val, int places) {
        try {
            long factor = (long) Math.pow(10, places);
            val = val * factor;
            long tmp = Math.round(val);
            return (double) tmp / factor;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return val;
        }
    }

    public static int getIndex(Spinner spinner, String str) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            System.out.println(i + "" + spinner.getItemAtPosition(i));
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(str)) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Takes a Date Object and an Integer value
     * adds value to the date
     * then returns new date
     */
    public static Date changeDate(Date date, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.DATE, value);
        return calendar.getTime();
    }


}
