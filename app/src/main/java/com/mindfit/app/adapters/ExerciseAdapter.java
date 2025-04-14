package com.mindfit.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mindfit.app.R;
import com.mindfit.app.models.Exercise;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> exercises;
    private OnExerciseClickListener listener;

    public interface OnExerciseClickListener {
        void onExerciseClick(Exercise exercise, int position);
        void onCompleteClick(Exercise exercise, int position);
    }

    public ExerciseAdapter(List<Exercise> exercises, OnExerciseClickListener listener) {
        this.exercises = exercises;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.bind(exercise, position);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void updateExercise(Exercise exercise, int position) {
        exercises.set(position, exercise);
        notifyItemChanged(position);
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivThumbnail;
        private TextView tvTitle;
        private TextView tvDuration;
        private TextView tvDescription;
        private ProgressBar progressBar;
        private ImageView ivCompleted;

        ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            progressBar = itemView.findViewById(R.id.progressBar);
            ivCompleted = itemView.findViewById(R.id.ivCompleted);
        }

        void bind(Exercise exercise, int position) {
            tvTitle.setText(exercise.getNom());
            tvDuration.setText(exercise.getDuree() + " min");
            tvDescription.setText(exercise.getDescription());

//            Glide.with(itemView.getContext())
//                    .load(exercise.getThumbnailUrl())
//                    .placeholder(R.drawable.placeholder_thumbnail)
//                    .into(ivThumbnail);
//
//            ivCompleted.setVisibility(exercise.isCompleted() ? View.VISIBLE : View.GONE);
//            progressBar.setVisibility(exercise.isCompleted() ? View.GONE : View.VISIBLE);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onExerciseClick(exercise, position);
                }
            });

            ivCompleted.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCompleteClick(exercise, position);
                }
            });
        }
    }
}
