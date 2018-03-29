package com.adam.camerawithsaveapi24.tools;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Date;

public class Utility {


    /**
    * https://gist.github.com/laaptu/7867851
    **/
    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    /**
     * https://gist.github.com/laaptu/7867851
     **/
    public static float convertPixelsToDp(float px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
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

    public static double roundSafely(double val, int places){
        try{
            long factor = (long) Math.pow(10, places);
            val = val *factor;
            long tmp = Math.round(val);
            return (double) tmp/factor;
        }catch (IllegalArgumentException e){
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
