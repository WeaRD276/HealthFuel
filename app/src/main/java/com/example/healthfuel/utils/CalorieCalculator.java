package com.example.healthfuel.utils;

import android.os.Build;

import com.example.healthfuel.model.UserData;
import com.example.healthfuel.model.CompetitionEntry;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalorieCalculator {

    public static class CalculationResult {
        public final double bmr;
        public final double tee;
        public final double proteins;
        public final double fats;
        public final double carbohydrates;

        public CalculationResult(double bmr, double tee, double proteins, double fats, double carbohydrates) {
            this.bmr = bmr;
            this.tee = tee;
            this.proteins = proteins;
            this.fats = fats;
            this.carbohydrates = carbohydrates;
        }
    }

    public static CalculationResult calculate(UserData user, List<CompetitionEntry> competitions) {
        int daysToEvent = getDaysToNextCompetition(competitions);

        double pal = getPAL(user.getSportType());

        if (daysToEvent == 0) {
            pal *= 1.1;
        } else if (daysToEvent <= 3) {
            pal *= 1.05;
        }

        double bmr = calculateBMR(user.getGender(), user.getAge(), user.getHeight(), user.getWeight());
        double tee = calculateTEE(bmr, pal);

        Map<String, Double> macros = getMacronutrientRecommendations(user.getSportType(), user.getWeight());

        if (daysToEvent == 0) {
            macros.put("carb", round(macros.get("carb") * 0.9, 1));
            macros.put("protein", round(macros.get("protein") * 1.1, 1));
        } else if (daysToEvent > 0 && daysToEvent <= 3) {
            macros.put("carb", round(macros.get("carb") * 1.2, 1));
        }

        return new CalculationResult(
                round(bmr, 1),
                round(tee, 1),
                macros.get("protein"),
                macros.get("fat"),
                macros.get("carb")
        );
    }

    private static int getDaysToNextCompetition(List<CompetitionEntry> competitions) {
        LocalDate today;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            today = LocalDate.now();
        } else {
            today = null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return competitions.stream()
                    .map(c -> LocalDate.parse(c.getDate()))
                    .filter(d -> !d.isBefore(today))
                    .mapToInt(d -> (int) ChronoUnit.DAYS.between(today, d))
                    .min()
                    .orElse(999);
        }
        return 999;
    }

    private static double calculateBMR(String gender, int age, double height, double weight) {
        if (gender.equalsIgnoreCase("male")) {
            return 66.5 + (13.75 * weight) + (5 * height) - (6.75 * age);
        } else {
            return 665.1 + (9.563 * weight) + (1.85 * height) - (4.676 * age);
        }
    }

    private static double calculateTEE(double bmr, double pal) {
        return bmr * pal;
    }

    private static double getPAL(String sportType) {
        switch (sportType.toLowerCase()) {
            case "football": return 1.7;
            case "swimming": return 1.9;
            case "running": return 1.8;
            case "aesthetic": return 1.5;
            default: return 1.4;
        }
    }

    private static Map<String, Double> getMacronutrientRecommendations(String sportType, double weight) {
        Map<String, Double> macros = new HashMap<>();

        switch (sportType.toLowerCase()) {
            case "football":
                macros.put("protein", round(weight * 1.6, 1));
                macros.put("fat", round(weight * 1.1, 1));
                macros.put("carb", round(weight * 7, 1));
                break;
            case "swimming":
                macros.put("protein", round(weight * 1.8, 1));
                macros.put("fat", round(weight * 1.2, 1));
                macros.put("carb", round(weight * 9, 1));
                break;
            case "running":
                macros.put("protein", round(weight * 1.4, 1));
                macros.put("fat", round(weight * 1.0, 1));
                macros.put("carb", round(weight * 10, 1));
                break;
            case "aesthetic":
                macros.put("protein", round(weight * 1.5, 1));
                macros.put("fat", round(weight * 0.8, 1));
                macros.put("carb", round(weight * 5, 1));
                break;
            default:
                macros.put("protein", round(weight * 1.4, 1));
                macros.put("fat", round(weight * 1.0, 1));
                macros.put("carb", round(weight * 5, 1));
                break;
        }

        return macros;
    }

    private static double round(double value, int decimals) {
        double factor = Math.pow(10, decimals);
        return Math.round(value * factor) / factor;
    }
}
