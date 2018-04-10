package com.adam.camerawithsaveapi24.tools;

import com.adam.camerawithsaveapi24.PhotoDisplayActivity;

public class NutritionCalculator {


//Original Harris-Benedict
/*
    BMR calculation for men (metric) 	BMR = 66.5 + ( 13.75 × weight in kg ) + ( 5.003 × height in cm ) – ( 6.755 × age in years )
    BMR calculation for men (imperial) 	BMR = 66 + ( 6.2 × weight in pounds ) + ( 12.7 × height in inches ) – ( 6.76 × age in years )
    BMR calculation for women (metric) 	BMR = 655.1 + ( 9.563 × weight in kg ) + ( 1.850 × height in cm ) – ( 4.676 × age in years )
    BMR calculation for women (imperial) 	BMR = 655.1 + ( 4.35 × weight in pounds ) + ( 4.7 × height in inches ) - ( 4.7 × age in years )
*/

//Revised Harris-Benedict
/*
    Men 	BMR = 88.362 + (13.397 × weight in kg) + (4.799 × height in cm) - (5.677 × age in years)
    Women 	BMR = 447.593 + (9.247 × weight in kg) + (3.098 × height in cm) - (4.330 × age in years)
*/

//    Activity Modifiers
/*
    Sedentary or light activity 	Office worker getting little or no exercise 	1.53 	BMR x 1.53
    Active or moderately active 	Construction worker or person running one hour daily 	1.76 	BMR x 1.76
    Vigorously active 	Agricultural worker (non mechanized) or person swimming two hours daily 	2.25 	BMR x 2.25
*/

    /**
     * Use Metric units only
     **/
    public static double CalcHarrisBenedictBMRMetric(String gender, double height, double weight, int age, String activityLevel, double goal) {
        double retVal = 0;

        if (gender.equalsIgnoreCase("male")) {
            retVal = 66.5 + (13.75 * weight) + (5.003 * height) - (6.755 * age);
        } else if (gender.equalsIgnoreCase("female")) {
            retVal = 655.1 + (9.563 * weight) + (1.850 * height) - (4.676 * age);
        }

        if (activityLevel.equalsIgnoreCase("sedentary") || activityLevel.equalsIgnoreCase("lightly")) {
            retVal = retVal * 1.53;
        } else if (activityLevel.equalsIgnoreCase("moderately")) {
            retVal = retVal * 1.76;
        } else if (activityLevel.equalsIgnoreCase("extremely")) {
            retVal = retVal * 2.25;
        }

        if (goal > weight) {
            retVal = (retVal - (retVal * 0.15));
        }

        return retVal;
    }

    /**
     * returns value in grams
     */
    public static double GetRecommendedSugarLimit(int age) {
        if (age < 13) {
            return 30;
        } else if (age < 70) {
            return 31;
        } else {
            return 28;
        }
    }


    /**
     * returns value in grams
     */
    public static double CalcRecommendedPercentProtein(double cals) {
//      30% of calories to grams
//      4 calories per gram of protein
        return (0.30 * cals) / 4;
    }

    /**
     * returns value in grams
     */
    public static double CalcRecommendedPercentFat(double cals) {
//      25% of calories to grams
//      9 calories per gram of fat
        return (0.25 * cals) / 9;
    }

    /**
     * returns value in grams
     */
    public static double CalcRecommendedPercentCarbohydrates(double cals) {//
//        45% or calories to grams
//        4 calories per gram of Carbohydrates
        return (0.45 * cals) / 4;
    }

    /**
     * returns value in miligrams
     */
    public static double CalcRecommendedPercentCalcium(int age) {
        if (age < 13) {
            return 1000;
        } else if (age < 18) {
            return 1300;
        } else if (age < 70) {
            return 1000;
        } else {
            return 1200;
        }
    }

    /**
     * returns value in grams
     */
    public static double GetRecommendedPercentSodium(int age) {
        if (age < 50) {
            return 1500;
        } else if (age < 70) {
            return 1300;
        } else {
            return 1200;
        }

    }

    /**
     * returns value in miligrams
     */
    public static double GetRecommendedPercentPotassium(int age) {
        if (age < 13) {
            return 4500;
        } else if (age < 70) {
            return 4700;
        } else {
            return 1200;
        }
    }

    /**
     * returns value in mg
     */
    public static double GetRecommendedIron(int age) {
        if (age < 13) {
            return 8;
        } else if (age < 18) {
            return 11;
        } else {
            return 8;
        }
    }

    /**
     * returns value in micrograms
     */
    public static double CalcRecommendedPercentVitaminA(int age, String gender) {
        if (age < 14) {
            return 600;
        } else {
            if (gender.equalsIgnoreCase("male")) {


                return 900;
            } else {
                return 700;
            }
        }
    }

    /**
     * returns value in micrograms
     */
    public static double CalcRecommendedPercentVitaminB1(String gender) {
        if (gender.equalsIgnoreCase("male")) {
            return 1200;
        } else {
            return 1100;
        }
    }

    /**
     * returns value in micrograms
     */
    public static double CalcRecommendedPercentVitaminB2(String gender) {
        if (gender.equalsIgnoreCase("male")) {
            return 1300;
        } else {
            return 1100;
        }
    }

    /**
     * returns value in micrograms
     */
    public static double CalcRecommendedPercentVitaminB3(String gender) {
        if (gender.equalsIgnoreCase("male")) {
            return 16;
        } else {
            return 14;
        }
    }


    /**
     * returns value in mg
     */
    public static double CalcRecommendedPercentVitaminB5() {
        return 5;
    }


    /**
     * returns value in mg
     */
    public static double CalcRecommendedPercentVitaminB6(int age, String gender) {
        if (age < 50) {
            return 1.3;
        } else {
            if (gender.equalsIgnoreCase("male")) {
                return 1.7;
            } else {
                return 1.5;
            }
        }
    }


    /**
     * returns value in micrograms
     */
    public static double CalcRecommendedPercentVitaminB9() {
        return 400;
    }


    /**
     * returns value in micrograms
     */
    public static double CalcRecommendedPercentVitaminB12() {
        return 2.4;
    }

    /**
     * returns value in mg
     */
    public static double CalcRecommendedPercentVitaminC(String gender) {
        if (gender.equalsIgnoreCase("male")) {
            return 90;
        } else {
            return 75;
        }
    }


    /**
     * returns value in mg
     */
    public static double CalcRecommendedPercentVitaminE() {
        return 15;
    }

    /**
     * returns value in micrograms
     */
    public static double CalcRecommendedPercentVitaminK(String gender) {
        if (gender.equalsIgnoreCase("male")) {
            return 120;
        } else {
            return 90;
        }
    }


    /**
     * returns value in micrograms
     */
    public static double CalcRecommendedPercentVitaminD(int age) {
        if (age < 70) {
            return 15;
        } else {
            return 20;
        }
    }


    /**
     * returns value in grams
     */
    public static double GetRecommendedPercentSaturatedFat(double cals) {
        return (0.1 * cals) / 9;
    }

    /**
     * returns value in grams
     */
    public static double GetRecommendedPercentFiber(int age) {
        if (age < 14) {
            return 31;
        } else {
            return 38;
        }
    }

    /**
     * returns value in miligrams
     */
    public static double GetRecommendedPercentCholesterol() {
        return 300;
    }


}
