package com.mindfit.app.models;

import java.io.Serializable;

public class Exercise implements Serializable {
    private String id;
    private String title;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private int duration; // in minutes
    private boolean completed;
    private String moodCategory;
    private String ageGroup;
    private String targetState;
    private int orderInPlaylist;

    public Exercise() {
    }

    public Exercise(String id, String title, String description, String videoUrl, 
                   String thumbnailUrl, int duration, String moodCategory, 
                   String ageGroup, String targetState, int orderInPlaylist) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.duration = duration;
        this.moodCategory = moodCategory;
        this.ageGroup = ageGroup;
        this.targetState = targetState;
        this.orderInPlaylist = orderInPlaylist;
        this.completed = false;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getMoodCategory() { return moodCategory; }
    public void setMoodCategory(String moodCategory) { this.moodCategory = moodCategory; }

    public String getAgeGroup() { return ageGroup; }
    public void setAgeGroup(String ageGroup) { this.ageGroup = ageGroup; }

    public String getTargetState() { return targetState; }
    public void setTargetState(String targetGender) { this.targetState = targetState; }

    public int getOrderInPlaylist() { return orderInPlaylist; }
    public void setOrderInPlaylist(int orderInPlaylist) { this.orderInPlaylist = orderInPlaylist; }
}
