package com.example.healthfuel.model;

public class FoodEntry {
    private String id;
    private String foodName;
    private int calories;
    private int proteins;
    private int fats;
    private int carbohydrates;

    public FoodEntry() {}

    public FoodEntry(String foodName, int calories, int proteins, int fats, int carbohydrates) {
        this.foodName = foodName;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }
    public int getProteins() { return proteins; }
    public void setProteins(int proteins) { this.proteins = proteins; }
    public int getFats() { return fats; }
    public void setFats(int fats) { this.fats = fats; }
    public int getCarbohydrates() { return carbohydrates; }
    public void setCarbohydrates(int carbohydrates) { this.carbohydrates = carbohydrates; }
}
