package com.mindfit.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.mindfit.app.adapters.ExerciseAdapter;
import com.mindfit.app.api.ApiClient;
import com.mindfit.app.models.Exercise;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeditationActivity extends AppCompatActivity implements ExerciseAdapter.OnExerciseClickListener {
    private TextView tvMoodSuggestion;
    private RecyclerView rvExercises;
    private ExerciseAdapter adapter;
    private List<Exercise> exercises = new ArrayList<>();
    private String currentMood;
    private String userAge;
    private String userGender;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer activePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);

        // Get user data from intent or preferences
        currentMood = getIntent().getStringExtra("mood");
        // TODO: Get these from SharedPreferences after user completes profile
        userAge = "25";
        userGender = "male";

        initViews();
        setupRecyclerView();
        setupYouTubePlayer();
        loadExercises();
    }

    private void initViews() {
        tvMoodSuggestion = findViewById(R.id.tvMoodSuggestion);
        rvExercises = findViewById(R.id.rvExercises);
        youTubePlayerView = findViewById(R.id.youtubePlayer);
        getLifecycle().addObserver(youTubePlayerView);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnChat).setOnClickListener(v -> startChatbot());
    }

    private void setupYouTubePlayer() {
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                activePlayer = youTubePlayer;
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new ExerciseAdapter(exercises, this);
        rvExercises.setLayoutManager(new LinearLayoutManager(this));
        rvExercises.setAdapter(adapter);
    }

    private void loadExercises() {
        ApiClient.getInstance()
                .getExercises(currentMood, userAge, userGender)
                .enqueue(new Callback<List<Exercise>>() {
                    @Override
                    public void onResponse(Call<List<Exercise>> call, Response<List<Exercise>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            exercises.clear();
                            exercises.addAll(response.body());
                            adapter.notifyDataSetChanged();
                            updateMoodSuggestion();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Exercise>> call, Throwable t) {
                        Toast.makeText(MeditationActivity.this, 
                            "Failed to load exercises: " + t.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateMoodSuggestion() {
        String suggestion = getMoodSuggestion(currentMood);
        tvMoodSuggestion.setText(suggestion);
    }

    @Override
    public void onExerciseClick(Exercise exercise, int position) {
        String videoId = extractVideoId(exercise.getVideoUrl());
        if (videoId != null && activePlayer != null) {
            youTubePlayerView.setVisibility(View.VISIBLE);
            activePlayer.loadVideo(videoId, 0);
        }
    }

    @Override
    public void onCompleteClick(Exercise exercise, int position) {
        exercise.setCompleted(true);
        adapter.updateExercise(exercise, position);

        ApiClient.getInstance()
                .updateExerciseProgress(exercise.getId(), true)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(MeditationActivity.this, 
                                "Progress updated!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        exercise.setCompleted(false);
                        adapter.updateExercise(exercise, position);
                        Toast.makeText(MeditationActivity.this,
                            "Failed to update progress", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String extractVideoId(String videoUrl) {
        // Example YouTube URL: https://www.youtube.com/watch?v=VIDEO_ID
        if (videoUrl != null && videoUrl.contains("v=")) {
            return videoUrl.split("v=")[1];
        }
        return null;
    }

    private void startChatbot() {
        startActivity(new Intent(this, ChatbotActivity.class));
    }

    private String getMoodSuggestion(String mood) {
        // Provide personalized suggestions based on mood
        switch (mood) {
            case "VERY_BAD":
                return "We understand you're going through a tough time. Let's focus on gentle, calming exercises to help lift your spirits.";
            case "BAD":
                return "When feeling down, it's important to be kind to yourself. We've selected some mood-lifting exercises.";
            case "NEUTRAL":
                return "Maintaining emotional balance is great! Let's explore some exercises to enhance your mood.";
            case "GOOD":
                return "It's wonderful that you're feeling good! Let's maintain this positive energy.";
            case "VERY_GOOD":
                return "Your positive mood is infectious! Let's channel this amazing energy into mindful exercises.";
            default:
                return "Let's take a moment to focus on your well-being with these exercises.";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }
}
