package com.example.projetmindfit.Dtos;

public class ProfileRequest {
    private String fullName;
    private int age;
    private String state;

    public ProfileRequest() {
    }

    public ProfileRequest(String fullName, int age, String state) {
        this.fullName = fullName;
        this.age = age;
        this.state = state;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
