package com.example.healthfuel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthfuel.model.CompetitionEntry;
import com.example.healthfuel.repository.CompetitionRepository;
import com.example.healthfuel.repository.UserRepository;
import com.example.healthfuel.utils.CalorieCalculator;
import com.example.healthfuel.model.UserData;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

public class HomeFragment extends Fragment {

    private TextView dailySummaryTextView;
    private UserRepository userRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dailySummaryTextView = view.findViewById(R.id.daily_summary_text_view);

        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("HealthFuelDB");
        userRepository = new UserRepository(database);

        populateHomePage("test@email.com");

        return view;
    }

    private void populateHomePage(String email) {
        Document userDoc = userRepository.getUserByEmail(email);
        if (userDoc == null) {
            dailySummaryTextView.setText("Користувача не знайдено");
            return;
        }

        int age = calculateAgeFromBirthdate(userDoc.getString("birthdate"));

        UserData user = new UserData(
                userDoc.getString("gender"),
                age,
                userDoc.getDouble("height"),
                userDoc.getDouble("weight"),
                userDoc.getString("sport"),
                email
        );

        MongoDatabase database = new MongoClient("localhost", 27017).getDatabase("HealthFuelDB");
        CompetitionRepository competitionRepository = new CompetitionRepository(database);
        List<CompetitionEntry> competitions = competitionRepository.getUserCompetitions(email);
        CalorieCalculator.CalculationResult result = CalorieCalculator.calculate(user, competitions);


        String summary = String.format(
                "Підсумок за день:\n\n" +
                        "BMR: %.1f ккал\n" +
                        "TEE: %.1f ккал\n\n" +
                        "Білки: %.1f г\n" +
                        "Жири: %.1f г\n" +
                        "Вуглеводи: %.1f г\n\n" +
                        "Вода: %.1f л (рекомендовано)",
                result.bmr,
                result.tee,
                result.proteins,
                result.fats,
                result.carbohydrates,
                2.0
        );

        dailySummaryTextView.setText(summary);
    }

    private int calculateAgeFromBirthdate(String birthdate) {
        try {
            int birthYear = Integer.parseInt(birthdate.split("-")[0]);
            return 2025 - birthYear;
        } catch (Exception e) {
            return 25;
        }
    }
}
