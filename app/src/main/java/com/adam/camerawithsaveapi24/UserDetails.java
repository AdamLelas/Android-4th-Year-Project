package com.adam.camerawithsaveapi24;


public class UserDetails {

    public enum ActivityLevel {
        Sedentary, Lightly, Moderately, Very, Extremely
    }

    private String username;
    private int age;
    private String gender;
    private double height;
    private double weight;
    private String activity_level;

    public double getGoal_weight() {
        return goal_weight;
    }

    public void setGoal_weight(double goal_weight) {
        this.goal_weight = goal_weight;
    }

    private double goal_weight;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getActivity_level() {
        return activity_level;
    }

    public void setActivity_level(String activity_level) {
        this.activity_level = activity_level;
    }

    public double calculateCalorieGoal() {


        return 1.0;
    }

}


