package com.example.healthfuel.model;

public class ExerciseEntry {
    private String id;
    private String exerciseName;
    private int duration;
    private int caloriesBurned;

    public ExerciseEntry() {}

    public ExerciseEntry(String exerciseName, int duration, int caloriesBurned) {
        this.exerciseName = exerciseName;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getExerciseName() { return exerciseName; }
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public int getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(int caloriesBurned) { this.caloriesBurned = caloriesBurned; }
}
