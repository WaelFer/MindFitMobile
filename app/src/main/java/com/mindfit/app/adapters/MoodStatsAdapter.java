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
import com.mindfit.app.models.MoodStat;
import java.util.List;

public class MoodStatsAdapter extends RecyclerView.Adapter<MoodStatsAdapter.ViewHolder> {
    private List<MoodStat> moodStats;
    private OnMoodStatClickListener listener;

    public interface OnMoodStatClickListener {
        void onMoodStatClick(MoodStat moodStat);
    }

    public MoodStatsAdapter(List<MoodStat> moodStats, OnMoodStatClickListener listener) {
        this.moodStats = moodStats;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mood_stat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MoodStat stat = moodStats.get(position);
        
        holder.tvMoodName.setText(stat.getMoodName());
        holder.tvPercentage.setText(String.format("%.1f%%", stat.getPercentage()));
        holder.tvProgress.setText(String.format("%d/%d videos completed", 
            stat.getCompletedVideos(), stat.getTotalVideos()));
        holder.tvLastDate.setText("Last: " + stat.getLastDate());
        
        holder.progressBar.setProgress((int) stat.getProgressPercentage());
        holder.progressBar.getProgressDrawable().setTint(stat.getColor());
        
        // Set mood emoji based on mood name
        int emojiResId = getMoodEmojiResource(stat.getMoodName());
        holder.ivMoodEmoji.setImageResource(emojiResId);
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMoodStatClick(stat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moodStats.size();
    }

    private int getMoodEmojiResource(String moodName) {
        switch (moodName.toUpperCase()) {
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
        TextView tvMoodName;
        TextView tvPercentage;
        TextView tvProgress;
        TextView tvLastDate;
        ProgressBar progressBar;

        ViewHolder(View view) {
            super(view);
            ivMoodEmoji = view.findViewById(R.id.ivMoodEmoji);
            tvMoodName = view.findViewById(R.id.tvMoodName);
            tvPercentage = view.findViewById(R.id.tvPercentage);
            tvProgress = view.findViewById(R.id.tvProgress);
            tvLastDate = view.findViewById(R.id.tvLastDate);
            progressBar = view.findViewById(R.id.progressBar);
        }
    }
}
