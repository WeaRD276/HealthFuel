package com.example.healthfuel.repository;

import com.example.healthfuel.model.ExerciseEntry;
import com.example.healthfuel.model.FoodEntry;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class TrackerRepository {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> foodCollection;
    private MongoCollection<Document> exerciseCollection;

    public TrackerRepository() {
        mongoClient = new MongoClient("localhost", 27017);
        database = mongoClient.getDatabase("HealthFuelDB");
        foodCollection = database.getCollection("foodEntries");
        exerciseCollection = database.getCollection("exerciseEntries");
    }

    public void addFoodEntry(FoodEntry entry) {
        Document doc = new Document("foodName", entry.getFoodName())
                .append("calories", entry.getCalories())
                .append("proteins", entry.getProteins())
                .append("fats", entry.getFats())
                .append("carbohydrates", entry.getCarbohydrates());
        foodCollection.insertOne(doc);
    }

    public void addExerciseEntry(ExerciseEntry entry) {
        Document doc = new Document("exerciseName", entry.getExerciseName())
                .append("duration", entry.getDuration())
                .append("caloriesBurned", entry.getCaloriesBurned());
        exerciseCollection.insertOne(doc);
    }

    public List<FoodEntry> getFoodEntries() {
        List<FoodEntry> entries = new ArrayList<>();
        for (Document doc : foodCollection.find()) {
            FoodEntry entry = new FoodEntry();
            entry.setFoodName(doc.getString("foodName"));
            entry.setCalories(doc.getInteger("calories"));
            entry.setProteins(doc.getInteger("proteins"));
            entry.setFats(doc.getInteger("fats"));
            entry.setCarbohydrates(doc.getInteger("carbohydrates"));
            entries.add(entry);
        }
        return entries;
    }

    public List<ExerciseEntry> getExerciseEntries() {
        List<ExerciseEntry> entries = new ArrayList<>();
        for (Document doc : exerciseCollection.find()) {
            ExerciseEntry entry = new ExerciseEntry();
            entry.setExerciseName(doc.getString("exerciseName"));
            entry.setDuration(doc.getInteger("duration"));
            entry.setCaloriesBurned(doc.getInteger("caloriesBurned"));
            entries.add(entry);
        }
        return entries;
    }
}
