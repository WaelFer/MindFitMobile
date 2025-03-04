package com.mindfit.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mindfit.app.fragments.HomeFragment;

public class HomeActivity extends AppCompatActivity {
    private ImageButton[] moodButtons;
    private TextInputEditText etMoodDescription;
    private MaterialButton btnConfirmMood;
    private int selectedMoodIndex = -1;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupListeners();
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.navigation_meditation) {
                // Launch meditation activity
                startActivity(new Intent(this, MeditationActivity.class));
                return true;
            } else if (itemId == R.id.navigation_chatbot) {
                // Launch chatbot activity
                startActivity(new Intent(this, ChatbotActivity.class));
                return true;
            } else if (itemId == R.id.navigation_stats) {
                // Launch stats activity
                startActivity(new Intent(this, StatsActivity.class));
                return true;
            }



            return false;
        });

        // Set default fragment

    }

    private void initViews() {
        moodButtons = new ImageButton[]{
            findViewById(R.id.btnMoodVeryBad),
            findViewById(R.id.btnMoodBad),
            findViewById(R.id.btnMoodNeutral),
            findViewById(R.id.btnMoodGood),
            findViewById(R.id.btnMoodVeryGood)
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
                // Navigate to meditation suggestions
                Intent intent = new Intent(this, MeditationActivity.class);
                intent.putExtra("mood_index", selectedMoodIndex);
                startActivity(intent);
            }
        });
    }

    private void selectMood(int index) {
        // Reset all buttons to default state
        for (ImageButton button : moodButtons) {
            button.setSelected(false);
        }
        // Select the clicked button
        moodButtons[index].setSelected(true);
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
}
