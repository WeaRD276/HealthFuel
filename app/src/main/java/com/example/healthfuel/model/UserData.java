package com.example.healthfuel.model;

public class UserData {
    private String gender;
    private int age;
    private double height;
    private double weight;
    private String sportType;
    private String email;

    public UserData(String gender, int age, double height, double weight, String sportType, String email) {
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.sportType = sportType;
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public String getSportType() {
        return sportType;
    }
    public String getEmail(){
        return email;
    }
}
