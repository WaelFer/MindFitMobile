package com.mindfit.app.models;

import java.io.Serializable;
enum etat {
    Sedentic,Actif,Athletic
}
public class Meditator implements Serializable {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private int age;
    private enum etat{};
}

