package com.mindfit.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetmindfit.Dtos.Etat;
import com.example.projetmindfit.Dtos.JwtRequest;
import com.example.projetmindfit.Dtos.JwtResponse;
import com.example.projetmindfit.Dtos.MeditantRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mindfit.app.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText etEmail, etNom, etPrenom, etPassword, etAge;
    private AutoCompleteTextView actvEtat;
    private MaterialButton btnSignUp;
    private TextView tvSignIn;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeViews();
        setupListeners();
        setupEtatDropdown();
    }

    private void initializeViews() {
        etEmail = findViewById(R.id.etEmail);
        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etPassword = findViewById(R.id.etPassword);
        etAge = findViewById(R.id.etAge);
        actvEtat = findViewById(R.id.actvEtat);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvSignIn = findViewById(R.id.tvSignIn);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupListeners() {
        btnSignUp.setOnClickListener(v -> handleSignUp());
        tvSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            finish();
        });
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void setupEtatDropdown() {
        String[] etats = new String[Etat.values().length];
        for (int i = 0; i < Etat.values().length; i++) {
            etats[i] = formatEtatForDisplay(Etat.values()[i]);
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_dropdown_item_1line, etats);
        actvEtat.setAdapter(adapter);
    }

    private String formatEtatForDisplay(Etat etat) {
        String name = etat.name().toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private Etat getEtatFromDisplayName(String displayName) {
        return Etat.valueOf(displayName.toUpperCase());
    }

    private void handleSignUp() {
        String email = etEmail.getText().toString().trim();
        String nom = etNom.getText().toString().trim();
        String prenom = etPrenom.getText().toString().trim();
        String password = etPassword.getText().toString();
        String ageStr = etAge.getText().toString().trim();
        String etatDisplay = actvEtat.getText().toString().trim();

        if (validateInputs(email, nom, prenom, password, ageStr, etatDisplay)) {
            int age = Integer.parseInt(ageStr);
            Etat etat = getEtatFromDisplayName(etatDisplay);
            MeditantRequest request = new MeditantRequest(email, nom, password, prenom, age, etat.getValue());
            performSignUp(request);
        }
    }

    private boolean validateInputs(String email, String nom, String prenom, 
                                 String password, String ageStr, String etatDisplay) {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Please enter a valid email address");
            return false;
        }
        if (nom.isEmpty()) {
            showError("Please enter your nom");
            return false;
        }
        if (prenom.isEmpty()) {
            showError("Please enter your prénom");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            showError("Password must be at least 6 characters");
            return false;
        }
        if (ageStr.isEmpty()) {
            showError("Please enter your age");
            return false;
        }
        try {
            int age = Integer.parseInt(ageStr);
            if (age < 13 || age > 120) {
                showError("Please enter a valid age between 13 and 120");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid age");
            return false;
        }
        if (etatDisplay.isEmpty()) {
            showError("Please select your état");
            return false;
        }
        try {
            getEtatFromDisplayName(etatDisplay);
        } catch (IllegalArgumentException e) {
            showError("Please select a valid état");
            return false;
        }
        return true;
    }

    private void performSignUp(MeditantRequest request) {
        btnSignUp.setEnabled(false);
        RetrofitClient.getApiService().register(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                btnSignUp.setEnabled(true);
                if (response.isSuccessful()) {
                    // After successful registration, try to authenticate
                    authenticateUser(request.getEmail(), request.getPassword());
                } else {
                    showError("Registration failed. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                btnSignUp.setEnabled(true);
                showError("Network error. Please check your connection.");
            }
        });
    }

    private void authenticateUser(String email, String password) {
        JwtRequest authRequest = new JwtRequest(email, password);
        RetrofitClient.getApiService().authenticate(authRequest).enqueue(new Callback<JwtResponse>() {
            @Override
            public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JwtResponse jwtResponse = response.body();
                    // Save token and navigate
                    Toast.makeText(SignUpActivity.this, 
                        "Registration successful!", 
                        Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                    finish();
                } else {
                    showError("Registration successful. You can sign in now.");
                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<JwtResponse> call, Throwable t) {
                showError("Registration successful but login failed. Please try signing in manually.");
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        });
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
