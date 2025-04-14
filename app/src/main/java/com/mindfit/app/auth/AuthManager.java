package com.mindfit.app.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.projetmindfit.Dtos.JwtRequest;
import com.example.projetmindfit.Dtos.JwtResponse;
import com.example.projetmindfit.Dtos.MeditantRequest;
import com.mindfit.app.HomeActivity;
import com.mindfit.app.network.AuthApi;
import com.mindfit.app.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthManager {

    private static final String TAG = "AuthManager";
    private AuthApi authApi;
    private Context context;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MindFitPrefs";
    private static final String KEY_JWT_TOKEN = "JwtToken";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PROFILE_COMPLETED = "ProfileCompleted";
    private static final String KEY_ROLE = "UserRole";
    private static final String KEY_USER_ID = "UserId";

    public AuthManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        authApi = RetrofitClient.getClient().create(AuthApi.class);
    }

    public void login(String email, String password) {
        JwtRequest jwtRequest = new JwtRequest(email, password);
        Call<JwtResponse> call = authApi.authenticate(jwtRequest);

        call.enqueue(new Callback<JwtResponse>() {
            @Override
            public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JwtResponse jwtResponse = response.body();
                    String token = jwtResponse.getToken();
                    String role = jwtResponse.getRole();
                    String userId = jwtResponse.getUserId();

                    // Check if the response contains an error message
                    if (token != null && !token.contains("Authentication failed")) {
                        Log.d(TAG, "Login successful: " + token);
                        saveToken(token);
                        saveEmail(email);
                        saveUserId(userId);
                        saveRole(role);
                        
                        // Set the token for Retrofit
                        RetrofitClient.setToken(token);
                        
                        // Always navigate to CompleteProfileActivity
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                        Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Login failed: " + token);
                        Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Login failed: " + response.message());
                    Toast.makeText(context, "Login failed: Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JwtResponse> call, Throwable t) {
                Log.e(TAG, "Login error: " + t.getMessage());
                Toast.makeText(context, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_JWT_TOKEN, token);
        editor.apply();
    }

    private void saveEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    private void saveUserId(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    private void saveRole(String role) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_JWT_TOKEN, null);
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public void fetchUserDetails() {
        // Get the token to check if we're logged in
        String token = getToken();
        if (token == null) {
            Log.e(TAG, "No token found - not logged in");
            return;
        }

        // Try to get the email from shared preferences
        String email = getEmail();
        if (email == null) {
            Log.e(TAG, "No email found in shared preferences");
            return;
        }

        Call<MeditantRequest> call = authApi.getUserDetails("Bearer " + token);
        call.enqueue(new Callback<MeditantRequest>() {
            @Override
            public void onResponse(Call<MeditantRequest> call, Response<MeditantRequest> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MeditantRequest userDetails = response.body();
                    if (userDetails.getEmail() != null) {
                        saveEmail(userDetails.getEmail());
                    }
                } else {
                    Log.e(TAG, "Failed to fetch user details: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MeditantRequest> call, Throwable t) {
                Log.e(TAG, "Failed to fetch user details: " + t.getMessage());
            }
        });
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public void setProfileCompleted(boolean completed) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_PROFILE_COMPLETED, completed);
        editor.apply();
    }

    public boolean isProfileCompleted() {
        return sharedPreferences.getBoolean(KEY_PROFILE_COMPLETED, false);
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_JWT_TOKEN);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_ROLE);
        editor.remove(KEY_PROFILE_COMPLETED);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.contains(KEY_JWT_TOKEN);
    }

    public String getRole() {
        return sharedPreferences.getString(KEY_ROLE, null);
    }
}
