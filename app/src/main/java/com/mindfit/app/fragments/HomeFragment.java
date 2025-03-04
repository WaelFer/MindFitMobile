package com.mindfit.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mindfit.app.R;
import com.mindfit.app.adapters.MoodHistoryAdapter;
import com.mindfit.app.api.ApiClient;
import com.mindfit.app.models.MoodEntry;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private ImageButton[] moodButtons;
    private TextInputEditText etMoodDescription;
    private MaterialButton btnConfirmMood;
    private RecyclerView rvMoodHistory;
    private MoodHistoryAdapter adapter;
    private List<MoodEntry> moodEntries = new ArrayList<>();
    private TextView tvProgress;
    private int selectedMoodIndex = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        setupRecyclerView();
        loadMoodHistory();
        return view;
    }

    private void initViews(View view) {
        moodButtons = new ImageButton[]{
            view.findViewById(R.id.btnVeryBad),
            view.findViewById(R.id.btnBad),
            view.findViewById(R.id.btnNeutral),
            view.findViewById(R.id.btnGood),
            view.findViewById(R.id.btnVeryGood)
        };

        etMoodDescription = view.findViewById(R.id.etMoodDescription);
        btnConfirmMood = view.findViewById(R.id.btnConfirmMood);
        rvMoodHistory = view.findViewById(R.id.rvMoodHistory);
        tvProgress = view.findViewById(R.id.tvProgress);

        for (int i = 0; i < moodButtons.length; i++) {
            final int index = i;
            moodButtons[i].setOnClickListener(v -> selectMood(index));
        }

        btnConfirmMood.setOnClickListener(v -> saveMoodEntry());
    }

    private void setupRecyclerView() {
        adapter = new MoodHistoryAdapter(moodEntries);
        rvMoodHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMoodHistory.setAdapter(adapter);
    }

    private void loadMoodHistory() {
        ApiClient.getInstance().getMoodHistory().enqueue(new Callback<List<MoodEntry>>() {
            @Override
            public void onResponse(Call<List<MoodEntry>> call, Response<List<MoodEntry>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    moodEntries.clear();
                    moodEntries.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    updateProgress();
                }
            }

            @Override
            public void onFailure(Call<List<MoodEntry>> call, Throwable t) {
                Toast.makeText(getContext(), 
                    "Failed to load mood history: " + t.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectMood(int index) {
        // Reset all buttons to default state
        for (ImageButton button : moodButtons) {
            button.setAlpha(0.5f);
        }
        // Highlight selected button
        moodButtons[index].setAlpha(1.0f);
        selectedMoodIndex = index;
    }

    private void saveMoodEntry() {
        if (selectedMoodIndex == -1) {
            Toast.makeText(getContext(), "Please select a mood", Toast.LENGTH_SHORT).show();
            return;
        }

        String description = etMoodDescription.getText() != null ? 
            etMoodDescription.getText().toString() : "";

        String[] moodValues = {"VERY_BAD", "BAD", "NEUTRAL", "GOOD", "VERY_GOOD"};
        MoodEntry entry = new MoodEntry(moodValues[selectedMoodIndex], description);

        ApiClient.getInstance().saveMoodEntry(entry).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Mood saved!", Toast.LENGTH_SHORT).show();
                    loadMoodHistory();
                    resetForm();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), 
                    "Failed to save mood: " + t.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetForm() {
        selectedMoodIndex = -1;
        for (ImageButton button : moodButtons) {
            button.setAlpha(0.5f);
        }
        etMoodDescription.setText("");
    }

    private void updateProgress() {
        ApiClient.getInstance().getProgress().enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int progress = response.body();
                    tvProgress.setText(String.format("Progress: %d%% completed", progress));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                tvProgress.setText("Progress: N/A");
            }
        });
    }
}
