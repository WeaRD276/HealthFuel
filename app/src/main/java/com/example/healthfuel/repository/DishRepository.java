package com.example.healthfuel.repository;

import com.example.healthfuel.model.ExerciseEntry;
import com.example.healthfuel.model.FoodEntry;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class DishRepository {
    private MongoCollection<Document> dishCollection;

    public DishRepository(MongoDatabase database) {
        dishCollection = database.getCollection("dishes");
    }

    public void addDish(String name, int mass, int proteins, int fats, int carbs, int calories) {
        Document doc = new Document("name", name)
                .append("mass", mass)
                .append("proteins", proteins)
                .append("fats", fats)
                .append("carbohydrates", carbs)
                .append("calories", calories);
        dishCollection.insertOne(doc);
    }

    public List<Document> getAllDishes() {
        return dishCollection.find().into(new ArrayList<>());
    }
}
