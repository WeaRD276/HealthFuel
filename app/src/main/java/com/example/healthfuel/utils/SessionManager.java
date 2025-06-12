package com.example.healthfuel.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "HealthFuelPrefs";
    private static final String KEY_EMAIL = "user_email";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserEmail(String email) {
        prefs.edit().putString(KEY_EMAIL, email).apply();
    }

    public String getUserEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }
}
