package com.example.healthfuel.repository;

import static com.mongodb.client.model.Filters.eq;

import com.example.healthfuel.model.ExerciseEntry;
import com.example.healthfuel.model.FoodEntry;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private MongoCollection<Document> userCollection;

    public UserRepository(MongoDatabase database) {
        userCollection = database.getCollection("users");
    }

    public void addUser(String email, String birthdate, String gender, int height, int weight, String sport) {
        Document doc = new Document("email", email)
                .append("birthdate", birthdate)
                .append("gender", gender)
                .append("height", height)
                .append("weight", weight)
                .append("sport", sport);
        userCollection.insertOne(doc);
    }

    public Document getUserByEmail(String email) {
        return userCollection.find(eq("email", email)).first();
    }

    public void updateWeight(String email, int newWeight) {
        userCollection.updateOne(eq("email", email), new Document("$set", new Document("weight", newWeight)));
    }
}
