package com.mindfit.app.models;

import java.io.Serializable;

public class Exercise implements Serializable {
    private int id;
    private String nom;
    private String description;
    private int duree;
    private String type;
    private String frequence_recommandee;
    private String exerciceHumeur;
    private String url;

    public Exercise() {
    }

    public Exercise(int id, String nom, String description, int duree,
                    String type, String frequence_recommandee,
                    String exerciceHumeur, String url) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.duree = duree;
        this.type = type;
        this.frequence_recommandee = frequence_recommandee;
        this.exerciceHumeur = exerciceHumeur;
        this.url = url;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getFrequence_recommandee() { return frequence_recommandee; }
    public void setFrequence_recommandee(String frequence_recommandee) {
        this.frequence_recommandee = frequence_recommandee;
    }

    public String getExerciceHumeur() { return exerciceHumeur; }
    public void setExerciceHumeur(String exerciceHumeur) {
        this.exerciceHumeur = exerciceHumeur;
    }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}