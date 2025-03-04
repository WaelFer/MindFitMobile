package com.mindfit.app.models;

public class MoodEntry {
    private String mood;
    private String description;
    private String date;
    private int completedVideos;
    private int totalVideos;

    public MoodEntry(String mood, String description) {
        this.mood = mood;
        this.description = description;
    }

    public String getMood() {
        return mood;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCompletedVideos() {
        return completedVideos;
    }

    public void setCompletedVideos(int completedVideos) {
        this.completedVideos = completedVideos;
    }

    public int getTotalVideos() {
        return totalVideos;
    }

    public void setTotalVideos(int totalVideos) {
        this.totalVideos = totalVideos;
    }

    public float getProgressPercentage() {
        return totalVideos > 0 ? (completedVideos * 100f) / totalVideos : 0f;
    }
}
