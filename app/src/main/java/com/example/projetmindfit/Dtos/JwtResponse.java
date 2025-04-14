package com.example.projetmindfit.Dtos;

public class JwtResponse {
    private String token;
    private String Role;
    private String userId;

    public JwtResponse() {
    }

    public JwtResponse(String token, String role, String userId) {
        this.token = token;
        this.Role = role;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        this.Role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
