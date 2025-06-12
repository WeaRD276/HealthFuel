package com.example.healthfuel;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import com.example.healthfuel.model.CompetitionEntry;
import com.example.healthfuel.repository.CompetitionRepository;
import com.example.healthfuel.utils.SessionManager;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;


public class CalendarFragment extends Fragment {

    private CalendarView calendarView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendar_view);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                handleDateSelection(year, month, dayOfMonth);
            }
        });

        return view;
    }

    private void handleDateSelection(int year, int month, int dayOfMonth) {
        String formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Додати змагання на " + formattedDate);

        final EditText input = new EditText(getContext());
        input.setHint("Назва змагання");
        builder.setView(input);

        builder.setPositiveButton("Зберегти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = input.getText().toString().trim();
                if (!title.isEmpty()) {
                    MongoClient mongoClient = new MongoClient("localhost", 27017);
                    MongoDatabase database = mongoClient.getDatabase("HealthFuelDB");
                    CompetitionRepository competitionRepo = new CompetitionRepository(database);

                    SessionManager sessionManager = new SessionManager(getContext());
                    String email = sessionManager.getUserEmail();
                    if (email != null) {
                        competitionRepo.addCompetition(email, new CompetitionEntry(title, formattedDate));
                    }

                    competitionRepo.addCompetition(email, new CompetitionEntry(title, formattedDate));
                    Toast.makeText(getContext(), "Змагання додано!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Назва не може бути порожньою", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Скасувати", null);
        builder.show();
    }


    private static class Utils {
        static long convertToMillis(int year, int month, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            return calendar.getTimeInMillis();
        }
    }



}
