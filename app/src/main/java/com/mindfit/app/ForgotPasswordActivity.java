package com.mindfit.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private MaterialButton btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initViews();
        setupListeners();
    }

    private void initViews() {
        tilEmail = findViewById(R.id.tilEmail);
        etEmail = findViewById(R.id.etEmail);
        btnSend = findViewById(R.id.btnSend);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void setupListeners() {
        btnSend.setOnClickListener(v -> {
            if (validateInput()) {
                // TODO: Implement actual password reset
                String email = etEmail.getText().toString().trim();
                // For now, just move to verify code screen
                Intent intent = new Intent(this, VerifyCodeActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }

    private boolean validateInput() {
        String email = etEmail.getText().toString().trim();
        
        if (email.isEmpty()) {
            tilEmail.setError("Email is required");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Invalid email format");
            return false;
        } else {
            tilEmail.setError(null);
            return true;
        }
    }
}
