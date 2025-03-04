package com.mindfit.app.api;

import com.mindfit.app.models.Exercise;
import com.mindfit.app.models.MoodEntry;
import com.mindfit.app.models.MoodStat;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @GET("api/exercises")
    Call<List<Exercise>> getExercises(
        @Query("mood") String mood,
        @Query("ageGroup") String ageGroup,
        @Query("gender") String gender
    );

    @POST("api/exercises/{id}/progress")
    Call<Void> updateExerciseProgress(
        @Path("id") String exerciseId,
        @Body boolean completed
    );

    @POST("api/chatbot")
    Call<String> getChatbotResponse(@Body String message);

    @GET("/api/mood/history")
    Call<List<MoodEntry>> getMoodHistory();

    @POST("/api/mood/save")
    Call<Void> saveMoodEntry(@Body MoodEntry entry);

    @GET("/api/mood/progress")
    Call<Integer> getProgress();

    @GET("mood/stats")
    Call<List<MoodStat>> getMoodStats();
}
