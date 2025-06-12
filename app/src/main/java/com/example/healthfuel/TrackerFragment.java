package com.example.healthfuel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthfuel.model.CompetitionEntry;
import com.example.healthfuel.model.ExerciseEntry;
import com.example.healthfuel.model.FoodEntry;
import com.example.healthfuel.repository.CompetitionRepository;
import com.example.healthfuel.repository.TrackerRepository;
import com.example.healthfuel.utils.CalorieCalculator;
import com.example.healthfuel.model.UserData;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.List;

public class TrackerFragment extends Fragment {

    private EditText foodEditText;
    private EditText foodMassEditText;
    private EditText exerciseEditText;
    private EditText exerciseDurationEditText;
    private EditText caloriesBurntEditText;
    private Button addFoodButton;
    private Button addExerciseButton;
    private TextView foodSummaryTextView;
    private TextView exerciseSummaryTextView;
    private TextView progressSummaryTextView;
    private TrackerRepository trackerRepository;
    private UserData currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracker, container, false);

        foodEditText = view.findViewById(R.id.food_edit_text);
        foodMassEditText = view.findViewById(R.id.food_mass_edit_text);
        exerciseEditText = view.findViewById(R.id.exercise_edit_text);
        exerciseDurationEditText = view.findViewById(R.id.exercise_duration_edit_text);
        caloriesBurntEditText = view.findViewById(R.id.calories_burnt_edit_text);
        addFoodButton = view.findViewById(R.id.add_food_button);
        addExerciseButton = view.findViewById(R.id.add_exercise_button);
        foodSummaryTextView = view.findViewById(R.id.food_summary_text_view);
        exerciseSummaryTextView = view.findViewById(R.id.exercise_summary_text_view);
        progressSummaryTextView = view.findViewById(R.id.progress_summary_text_view);

        trackerRepository = new TrackerRepository();

        fetchUserData();

        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFoodEntry();
            }
        });

        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExerciseEntry();
            }
        });

        return view;
    }

    private void fetchUserData() {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("HealthFuelDB");
        MongoCollection<Document> userCollection = database.getCollection("users");
        Document userDoc = userCollection.find(new Document("email", "test@email.com")).first();

        if (userDoc != null) {
            String gender = userDoc.getString("gender");
            int age = 2025 - Integer.parseInt(userDoc.getString("birthdate").split("-")[0]);
            double height = userDoc.getInteger("height").doubleValue();
            double weight = userDoc.getInteger("weight").doubleValue();
            String sport = userDoc.getString("sport");
            String email = userDoc.getString("email");
            currentUser = new UserData(gender, age, height, weight, sport, email);
        }
    }

    private void addFoodEntry() {
        String foodName = foodEditText.getText().toString();
        int mass = Integer.parseInt(foodMassEditText.getText().toString());
        FoodEntry foodEntry = new FoodEntry(foodName, mass * 2, 10, 5, 15);
        trackerRepository.addFoodEntry(foodEntry);
        populateFoodSummary();
        populateProgressSummary();
        foodEditText.setText("");
        foodMassEditText.setText("");
    }

    private void addExerciseEntry() {
        String exerciseName = exerciseEditText.getText().toString();
        int duration = Integer.parseInt(exerciseDurationEditText.getText().toString());
        int calories = Integer.parseInt(caloriesBurntEditText.getText().toString());
        ExerciseEntry exerciseEntry = new ExerciseEntry(exerciseName, duration, calories);
        trackerRepository.addExerciseEntry(exerciseEntry);
        populateExerciseSummary();
        populateProgressSummary();
        exerciseEditText.setText("");
        exerciseDurationEditText.setText("");
        caloriesBurntEditText.setText("");
    }

    private void populateFoodSummary() {
        List<FoodEntry> foodEntries = trackerRepository.getFoodEntries();
        StringBuilder summary = new StringBuilder("Підсумок їжі:\n");
        for (FoodEntry entry : foodEntries) {
            summary.append("- ").append(entry.getFoodName())
                    .append(" (Ккал: ").append(entry.getCalories()).append(")\n");
        }
        foodSummaryTextView.setText(summary.toString());
    }

    private void populateExerciseSummary() {
        List<ExerciseEntry> exerciseEntries = trackerRepository.getExerciseEntries();
        StringBuilder summary = new StringBuilder("Підсумок вправ:\n");
        for (ExerciseEntry entry : exerciseEntries) {
            summary.append("- ").append(entry.getExerciseName())
                    .append(" (Тривалість: ").append(entry.getDuration())
                    .append(" хв, Ккал: ").append(entry.getCaloriesBurned()).append(")\n");
        }
        exerciseSummaryTextView.setText(summary.toString());
    }

    private void populateProgressSummary() {
        int consumed = trackerRepository.getFoodEntries().stream().mapToInt(FoodEntry::getCalories).sum();
        int burned = trackerRepository.getExerciseEntries().stream().mapToInt(ExerciseEntry::getCaloriesBurned).sum();

        StringBuilder progress = new StringBuilder("Прогрес за день:\n");
        progress.append("Спожито: ").append(consumed).append(" ккал\n");
        progress.append("Спалено: ").append(burned).append(" ккал\n");

        if (currentUser != null) {
            MongoDatabase database = new MongoClient("localhost", 27017).getDatabase("HealthFuelDB");
            CompetitionRepository competitionRepository = new CompetitionRepository(database);
            List<CompetitionEntry> competitions = competitionRepository.getUserCompetitions(currentUser.getEmail());
            CalorieCalculator.CalculationResult result = CalorieCalculator.calculate(currentUser, competitions);

            progress.append("Рекомендовано: ").append((int) result.tee).append(" ккал\n");
        }

        progressSummaryTextView.setText(progress.toString());
    }

}
