package com.mindfit.app.api;

import com.mindfit.app.models.Exercise;
import com.mindfit.app.models.MoodEntry;
import com.mindfit.app.models.MoodStat;

import java.util.List;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:3000/"; // For Android Emulator
    private static ApiClient instance;
    private final ApiService apiService;

    private ApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public Call<List<Exercise>> getExercises(String mood, String ageGroup, String gender) {
        return apiService.getExercises(mood, ageGroup, gender);
    }

    public Call<Void> updateExerciseProgress(String exerciseId, boolean completed) {
        return apiService.updateExerciseProgress(exerciseId, completed);
    }

    public Call<String> getChatbotResponse(String message) {
        return apiService.getChatbotResponse(message);
    }

    public Call<List<MoodEntry>> getMoodHistory() {
        return apiService.getMoodHistory();
    }

    public Call<Void> saveMoodEntry(MoodEntry entry) {
        return apiService.saveMoodEntry(entry);
    }

    public Call<Integer> getProgress() {
        return apiService.getProgress();
    }
    public Call<List<MoodStat>> getMoodStats() {
        return apiService.getMoodStats();
    }
}
