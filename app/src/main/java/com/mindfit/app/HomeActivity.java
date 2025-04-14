package com.mindfit.app;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mindfit.app.auth.AuthManager;
import com.mindfit.app.fragments.HomeFragment;

public class HomeActivity extends AppCompatActivity {
    private ImageButton[] moodButtons;
    private TextInputEditText etMoodDescription;
    private MaterialButton btnConfirmMood;
    private int selectedMoodIndex = -1;
    private BottomNavigationView bottomNavigation;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        authManager = new AuthManager(this);

        initViews();
        setupListeners();
        setupBottomNavigation();
    }

    private void initViews() {
        moodButtons = new ImageButton[]{
            findViewById(R.id.btnMoodTired),
            findViewById(R.id.btnMoodAngry),
            findViewById(R.id.btnMoodRelaxed),
            findViewById(R.id.btnMoodHappy)
        };
        etMoodDescription = findViewById(R.id.etMoodDescription);
        btnConfirmMood = findViewById(R.id.btnConfirmMood);
    }

    private void setupListeners() {
        for (int i = 0; i < moodButtons.length; i++) {
            final int index = i;
            moodButtons[i].setOnClickListener(v -> selectMood(index));
        }

        btnConfirmMood.setOnClickListener(v -> {
            if (selectedMoodIndex != -1) {
                String description = etMoodDescription.getText().toString().trim();
                saveMoodEntry(selectedMoodIndex, description);

                // Get mood string
                String mood = getMoodString(selectedMoodIndex);

                // Create intent with mood data
                Intent intent = new Intent(this, MeditationActivity.class);
                intent.putExtra("mood_index", selectedMoodIndex);
                intent.putExtra("mood_string", mood);
                startActivity(intent);
            }
        });
    }

    private void setupBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.navigation_chatbot) {
                startActivity(new Intent(this, ChatbotActivity.class));
                return true;
            } else if (itemId == R.id.navigation_stats) {
                startActivity(new Intent(this, StatsActivity.class));
                return true;
            } else if (itemId == R.id.navigation_profile) {
                Intent intent = new Intent(this, UpdateProfileActivity.class);
                intent.putExtra("token", authManager.getToken());
                String email = authManager.getEmail();
                if (email == null) {
                    // Try to fetch email from server
                    authManager.fetchUserDetails();
                    // Wait a moment for the email to be fetched
                    try {
                        Thread.sleep(1000); // Wait 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    email = authManager.getEmail();
                }
                if (email == null) {
                    Toast.makeText(this, "Erreur: Email non disponible", Toast.LENGTH_SHORT).show();
                    return true;
                }
                intent.putExtra("email", email);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_logout) {
                authManager.logout();
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            return false;
        });
    }

    private void selectMood(int index) {
        for (int i = 0; i < moodButtons.length; i++) {
            moodButtons[i].setSelected(i == index);
        }
        selectedMoodIndex = index;
    }

    private void saveMoodEntry(int moodIndex, String description) {
        // TODO: Save mood entry to local database or backend
        String[] moodLabels = {"Very Bad", "Bad", "Neutral", "Good", "Very Good"};
        String mood = moodLabels[moodIndex];
        // For now, just show a success message
        String message = "Mood saved: " + mood;
        if (!description.isEmpty()) {
            message += "\nDescription: " + description;
        }
        // TODO: Send this data to backend
    }

    private String getMoodString(int moodIndex) {
        switch (moodIndex) {
            case 0: return "Tired";
            case 1: return "Angry";
            case 2: return "Relaxed";
            case 3: return "Content";
            default: return "";
        }
    }
}
