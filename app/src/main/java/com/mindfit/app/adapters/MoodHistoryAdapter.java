package com.mindfit.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mindfit.app.R;
import com.mindfit.app.models.MoodEntry;
import java.util.List;

public class MoodHistoryAdapter extends RecyclerView.Adapter<MoodHistoryAdapter.ViewHolder> {
    private List<MoodEntry> moodEntries;

    public MoodHistoryAdapter(List<MoodEntry> moodEntries) {
        this.moodEntries = moodEntries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mood_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MoodEntry entry = moodEntries.get(position);
        
        holder.tvDate.setText(entry.getDate());
        holder.tvDescription.setText(entry.getDescription());
        holder.tvProgress.setText(String.format("%d/%d videos completed", 
            entry.getCompletedVideos(), entry.getTotalVideos()));
        
        holder.progressBar.setProgress((int) entry.getProgressPercentage());
        
        // Set mood emoji based on mood
        int emojiResId = getMoodEmojiResource(entry.getMood());
        holder.ivMoodEmoji.setImageResource(emojiResId);
    }

    @Override
    public int getItemCount() {
        return moodEntries.size();
    }

    private int getMoodEmojiResource(String mood) {
        switch (mood.toUpperCase()) {
            case "VERY_BAD":
                return R.drawable.emoji_very_bad;
            case "BAD":
                return R.drawable.emoji_bad;
            case "NEUTRAL":
                return R.drawable.emoji_neutral;
            case "GOOD":
                return R.drawable.emoji_good;
            case "VERY_GOOD":
                return R.drawable.emoji_very_good;
            default:
                return R.drawable.emoji_neutral;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMoodEmoji;
        TextView tvDate;
        TextView tvDescription;
        TextView tvProgress;
        ProgressBar progressBar;

        ViewHolder(View view) {
            super(view);
            ivMoodEmoji = view.findViewById(R.id.ivMoodEmoji);
            tvDate = view.findViewById(R.id.tvDate);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvProgress = view.findViewById(R.id.tvProgress);
            progressBar = view.findViewById(R.id.progressBar);
        }
    }
}
