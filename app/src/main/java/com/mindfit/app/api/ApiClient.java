package com.mindfit.app.api;

import com.mindfit.app.models.Exercise;
import com.mindfit.app.models.Meditator;
import com.mindfit.app.models.MoodEntry;
import com.mindfit.app.models.MoodStat;

import java.util.List;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:8082/"; // Replace with your actual local IP address
    private static Retrofit retrofit = null;
    private static ApiService apiService = null;

    public static ApiService getInstance() {
        if (apiService == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    public static Call<List<Exercise>> getExercises(String mood, String ageGroup, String gender) {
        return getInstance().getExercises(mood, ageGroup, gender);
    }

    public static Call<Void> updateExerciseProgress(String exerciseId, boolean completed) {
        return getInstance().updateExerciseProgress(exerciseId, completed);
    }

    public static Call<String> getChatbotResponse(String message) {
        return getInstance().getChatbotResponse(message);
    }

    public static Call<List<MoodEntry>> getMoodHistory() {
        return getInstance().getMoodHistory();
    }

    public static Call<Void> saveMoodEntry(MoodEntry entry) {
        return getInstance().saveMoodEntry(entry);
    }

    public static Call<Void> updateProfile(String email, Meditator profileData) {
        return getInstance().updateProfile(email, profileData);
    }

    public static Call<Meditator> getUserProfile(String email) {
        return getInstance().getUserProfile(email);
    }

    public static Call<Integer> getProgress() {
        return getInstance().getProgress();
    }
    public static Call<List<MoodStat>> getMoodStats() {
        return getInstance().getMoodStats();
    }
}
