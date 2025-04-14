package com.mindfit.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mindfit.app.api.ApiService;
import com.mindfit.app.network.RetrofitClient;
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

public class MeditationActivity extends AppCompatActivity  {
    private TextView tvMoodSuggestion;
    private Button btn;
    private RecyclerView rvExercises;
    private ExerciseAdapter adapter;
    private List<Exercise> exercises = new ArrayList<>();
    private String currentMood;
    private String userAge;
    private String userGender;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);
        initViews();
        setupRecyclerView();
        loadExercises();

    }

    private void initViews() {
        btn = findViewById(R.id.play);
        tvMoodSuggestion = findViewById(R.id.tvMoodSuggestion);
        rvExercises = findViewById(R.id.rvExercises);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void playVideo(String videoUrl) {
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer player) {
                youTubePlayer = player;
                String videoId = extractVideoId(videoUrl);
                if (videoId != null) {
                    Toast.makeText(MeditationActivity.this, videoUrl, Toast.LENGTH_SHORT).show();
                    youTubePlayer.loadVideo(videoId, 0);

                } else {
                    Toast.makeText(MeditationActivity.this,"error"+ videoUrl, Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private String extractVideoId(String url) {
        String videoId = null;
        if (url != null && url.trim().length() > 0) {
            String[] urlParts = url.split("v=");
            if (urlParts.length > 1) {
                videoId = urlParts[1].split("&")[0];
            }
        }
        return videoId;
    }
    private void loadExercises() {
        String mood = getIntent().getStringExtra("mood_string");
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Exercise>> call = apiService.getMeditationExercises(mood);
        call.enqueue(new Callback<List<Exercise>>() {
            @Override
            public void onResponse(Call<List<Exercise>> call, Response<List<Exercise>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    exercises.clear();
                    exercises.addAll(response.body());
                    loadRecycle(exercises);
                } else {
                    Log.e("MeditationActivity", "Failed to load exercises");
                }
            }

            @Override
            public void onFailure(Call<List<Exercise>> call, Throwable t) {
                Log.e("MeditationActivity", "Error: " + t.getMessage());
            }
        });
    }

    private void loadRecycle(List<Exercise> exs) {
        adapter = new ExerciseAdapter(exs, new ExerciseAdapter.OnExerciseClickListener() {
            @Override
            public void onExerciseClick(Exercise exercise, int position) {
                String videoUrl = exercise.getUrl(); // Get the URL from the exercise

                if (videoUrl != null && !videoUrl.isEmpty()) {

                    playVideo(videoUrl); // Call playVideo to set the URL
                } else {
                    Toast.makeText(MeditationActivity.this, "No video available", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCompleteClick(Exercise exercise, int position) {
                Toast.makeText(MeditationActivity.this, "Completed: " + exercise.getNom(), Toast.LENGTH_SHORT).show();
            }
        });
        setupRecyclerView();
    }
    private void setupRecyclerView() {
        rvExercises.setLayoutManager(new LinearLayoutManager(this));
        rvExercises.setAdapter(adapter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }

    private void updateMoodSuggestion() {
        String suggestion = getMoodSuggestion(currentMood);
        tvMoodSuggestion.setText(suggestion);
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

}
