package com.example.healthfuel;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.healthfuel.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SessionManager sessionManager = new SessionManager(getContext());
//        sessionManager.saveUserEmail("test@email.com");


        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                Fragment selectedFragment = null;

                final int id = item.getItemId();
                if (id == R.id.nav_home)
                    selectedFragment = new HomeFragment();
                else if (id == R.id.nav_tracker)
                    selectedFragment = new TrackerFragment();
                else if (id == R.id.nav_calendar)
                    selectedFragment = new CalendarFragment();
                else if (id == R.id.nav_profile)
                    selectedFragment = new ProfileFragment();

                if (selectedFragment != null) {
                    switchFragment(selectedFragment);
                }
                return true;
            }
        });
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
