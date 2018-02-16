package com.adam.camerawithsaveapi24;

/**
 * Created by Adam on 30/01/2018.
 */

public class Singleton {
    private static final Singleton ourInstance = new Singleton();

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
    }
}
