package com.example.healthfuel;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private DatePicker dateOfBirthPicker;
    private EditText genderEditText;
    private EditText heightEditText;
    private EditText weightEditText;
    private EditText sportEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        dateOfBirthPicker = findViewById(R.id.dateOfBirth);
        genderEditText = findViewById(R.id.gender);
        heightEditText = findViewById(R.id.height);
        weightEditText = findViewById(R.id.weight);
        sportEditText = findViewById(R.id.sport);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegistration();
            }
        });
    }

    private void handleRegistration() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        int day = dateOfBirthPicker.getDayOfMonth();
        int month = dateOfBirthPicker.getMonth();
        int year = dateOfBirthPicker.getYear();
        String gender = genderEditText.getText().toString().trim();
        String height = heightEditText.getText().toString().trim();
        String weight = weightEditText.getText().toString().trim();
        String sport = sportEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || gender.isEmpty() ||
                height.isEmpty() || weight.isEmpty() || sport.isEmpty()) {
            Toast.makeText(this, "Заповніть усі поля", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Реєстрація успішна!", Toast.LENGTH_SHORT).show();
    }
}