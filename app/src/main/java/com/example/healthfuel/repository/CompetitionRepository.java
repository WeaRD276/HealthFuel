package com.example.healthfuel.repository;

import com.example.healthfuel.model.CompetitionEntry;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class CompetitionRepository {
    private final MongoCollection<Document> competitionCollection;

    public CompetitionRepository(MongoDatabase database) {
        this.competitionCollection = database.getCollection("competitions");
    }

    public void addCompetition(String email, CompetitionEntry entry) {
        Document doc = new Document("email", email)
                .append("title", entry.getTitle())
                .append("date", entry.getDate());
        competitionCollection.insertOne(doc);
    }

    public List<CompetitionEntry> getUserCompetitions(String email) {
        List<CompetitionEntry> competitions = new ArrayList<>();
        for (Document doc : competitionCollection.find(new Document("email", email))) {
            competitions.add(new CompetitionEntry(doc.getString("title"), doc.getString("date")));
        }
        return competitions;
    }
}
