package com.mindfit.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mindfit.app.auth.AuthManager;

public class SignInActivity extends AppCompatActivity {
    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnSignIn;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        AuthManager authManager = new AuthManager(this);

        // Check if user is already logged in
        if (authManager.isLoggedIn()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();

        }
        initViews();
        setupListeners();
    }

    private void initViews() {
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);

       findViewById(R.id.tvForgotPassword).setOnClickListener(v ->
         startActivity(new Intent(this, ForgotPasswordActivity.class)));
        
        findViewById(R.id.tvSignUp).setOnClickListener(v -> 
            startActivity(new Intent(this, SignUpActivity.class)));
    }

    private void setupListeners() {
        authManager = new AuthManager(this);
        btnSignIn.setOnClickListener(v -> {
            if (validateInput()) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                authManager.login(email, password);
            }
        });

        // Social login buttons

        findViewById(R.id.btnGoogle).setOnClickListener(v -> handleSocialLogin("Google"));
        findViewById(R.id.btnFacebook).setOnClickListener(v -> handleSocialLogin("Facebook"));
    }

    private boolean validateInput() {
        boolean isValid = true;
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            tilEmail.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Invalid email format");
            isValid = false;
        } else {
            tilEmail.setError(null);
        }

        if (password.isEmpty()) {
            tilPassword.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            tilPassword.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            tilPassword.setError(null);
        }

        return isValid;
    }

    private void handleSocialLogin(String provider) {
        // TODO: Implement social login
        Toast.makeText(this, provider + " login clicked", Toast.LENGTH_SHORT).show();
    }
}
