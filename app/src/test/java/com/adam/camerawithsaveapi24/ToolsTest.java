package com.adam.camerawithsaveapi24;

import com.adam.camerawithsaveapi24.classes.FoodItem;
import com.adam.camerawithsaveapi24.tools.NutritionCalculator;
import com.adam.camerawithsaveapi24.tools.Utility;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class ToolsTest {
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

    @Test
    public void combineFoodItemsTest() throws Exception {
        FoodItem f1 = new FoodItem("1","1","1","1","1","1","1","1","1","1","1","1", "1");
        FoodItem f2 = new FoodItem("2","2","2","2","2","2","2","2","2","2","2","2", "1");
        FoodItem expected = new FoodItem("1, with 2", "3","3","3","3","3","3","3","3","3","3","3","1");
        expected.setServings(0);
        FoodItem output = Utility.combineFoodItems(f1,f2);
        assertEquals(expected, output);
    }




}