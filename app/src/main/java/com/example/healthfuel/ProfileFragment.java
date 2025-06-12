package com.example.healthfuel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.healthfuel.repository.UserRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class ProfileFragment extends Fragment {

    private EditText emailEditText;
    private EditText birthDateEditText;
    private EditText genderEditText;
    private EditText heightEditText;
    private EditText weightEditText;
    private Spinner sportSpinner;
    private Button saveButton;
    private UserRepository userRepository;

    private final String[] sports = {"Футбол", "Плавання"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        emailEditText = view.findViewById(R.id.email_edit_text);
        birthDateEditText = view.findViewById(R.id.birth_date_edit_text);
        genderEditText = view.findViewById(R.id.gender_edit_text);
        heightEditText = view.findViewById(R.id.height_edit_text);
        weightEditText = view.findViewById(R.id.weight_edit_text);
        sportSpinner = view.findViewById(R.id.sport_spinner);
        saveButton = view.findViewById(R.id.save_button);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sports);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSpinner.setAdapter(adapter);

        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("HealthFuelDB");
        userRepository = new UserRepository(database);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });

        loadUserProfile("user@example.com");

        return view;
    }

    private void loadUserProfile(String email) {
        Document userDoc = userRepository.getUserByEmail(email);
        if (userDoc != null) {
            emailEditText.setText(userDoc.getString("email"));
            birthDateEditText.setText(userDoc.getString("birthdate"));
            genderEditText.setText(userDoc.getString("gender"));
            heightEditText.setText(String.valueOf(userDoc.getInteger("height")));
            weightEditText.setText(String.valueOf(userDoc.getInteger("weight")));

            String sport = userDoc.getString("sport");
            int position = ((ArrayAdapter<String>) sportSpinner.getAdapter()).getPosition(sport);
            if (position >= 0) sportSpinner.setSelection(position);
        } else {
            Toast.makeText(getContext(), "Користувача не знайдено", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserProfile() {
        try {
            String email = emailEditText.getText().toString().trim();
            String birthDate = birthDateEditText.getText().toString().trim();
            String gender = genderEditText.getText().toString().trim();
            int height = Integer.parseInt(heightEditText.getText().toString().trim());
            int weight = Integer.parseInt(weightEditText.getText().toString().trim());
            String sport = sportSpinner.getSelectedItem().toString();

            userRepository.addUser(email, birthDate, gender, height, weight, sport);
            Toast.makeText(getContext(), "Дані збережено!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Помилка при збереженні: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
