package com.adam.camerawithsaveapi24.tools;

import org.junit.Test;

import static org.junit.Assert.*;


public class NutritionCalculatorTest {
    @Test
    public void TestCalcHarrisBenedictBMRMetric() throws Exception {
        double output;
        double expected= 2732;
        int inputAge=52;
        double inputGoal=80, inputWeight=96, inputHeight=150;
        String inputGender="male", inputActivityLevel="lightly";
        double delta =0.15;
        NutritionCalculator nc = new NutritionCalculator();


        output = nc.CalcHarrisBenedictBMRMetric(inputGender, inputHeight, inputWeight, inputAge, inputActivityLevel, inputGoal);

        assertEquals(expected, output, delta);

    }
//
//    @Test
//    public void calcRecommendedPercentProtein() throws Exception {
//    }
//
//    @Test
//    public void calcRecommendedPercentFat() throws Exception {
//    }
//
//    @Test
//    public void calcRecommendedPercentCarbohydrates() throws Exception {
//    }
//
//    @Test
//    public void calcRecommendedPercentCalcium() throws Exception {
//    }

}