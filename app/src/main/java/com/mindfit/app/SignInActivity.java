package com.mindfit.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignInActivity extends AppCompatActivity {
    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

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
        btnSignIn.setOnClickListener(v -> {
            if (validateInput()) {
                // TODO: Implement actual authentication
                startActivity(new Intent(this, HomeActivity.class));
                finish();
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
