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

public class ProductRepository {
    private MongoCollection<Document> productCollection;

    public ProductRepository(MongoDatabase database) {
        productCollection = database.getCollection("products");
    }

    public void addProduct(String name, int proteins, int fats, int carbohydrates, int calories, String type) {
        Document doc = new Document("name", name)
                .append("proteins", proteins)
                .append("fats", fats)
                .append("carbohydrates", carbohydrates)
                .append("calories", calories)
                .append("type", type);
        productCollection.insertOne(doc);
    }

    public List<Document> findProductsByType(String type) {
        return productCollection.find(eq("type", type)).into(new ArrayList<>());
    }

    public Document getProductByName(String name) {
        return productCollection.find(eq("name", name)).first();
    }
}
