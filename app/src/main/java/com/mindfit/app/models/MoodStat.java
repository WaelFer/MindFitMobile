package com.mindfit.app.models;

public class MoodStat {
    private String moodName;
    private float percentage;
    private int color;
    private int completedVideos;
    private int totalVideos;
    private String lastDate;

    public MoodStat(String moodName, float percentage, int color, int completedVideos, int totalVideos, String lastDate) {
        this.moodName = moodName;
        this.percentage = percentage;
        this.color = color;
        this.completedVideos = completedVideos;
        this.totalVideos = totalVideos;
        this.lastDate = lastDate;
    }

    public String getMoodName() {
        return moodName;
    }

    public float getPercentage() {
        return percentage;
    }

    public int getColor() {
        return color;
    }

    public int getCompletedVideos() {
        return completedVideos;
    }

    public int getTotalVideos() {
        return totalVideos;
    }

    public String getLastDate() {
        return lastDate;
    }

    public float getProgressPercentage() {
        return totalVideos > 0 ? (completedVideos * 100f) / totalVideos : 0f;
    }
}
