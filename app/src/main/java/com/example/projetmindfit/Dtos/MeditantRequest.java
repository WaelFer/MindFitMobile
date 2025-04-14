package com.example.projetmindfit.Dtos;

public class MeditantRequest {
    private String email;
    private String nom;
    private String password;
    private String prenom;
    private int age;
    private int etat;

    public MeditantRequest() {
    }

    public MeditantRequest(String email, String nom, String password, String prenom, int age, int etat) {
        this.email = email;
        this.nom = nom;
        this.password = password;
        this.prenom = prenom;
        this.age = age;
        this.etat = etat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }
}
