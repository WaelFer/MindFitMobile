package com.mindfit.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class VerifyCodeActivity extends AppCompatActivity {
    private EditText[] codeInputs = new EditText[4];
    private MaterialButton btnVerify;
    private TextView tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        String email = getIntent().getStringExtra("email");
        initViews(email);
        setupCodeInputs();
        setupListeners();
    }

    private void initViews(String email) {
        codeInputs[0] = findViewById(R.id.etCode1);
        codeInputs[1] = findViewById(R.id.etCode2);
        codeInputs[2] = findViewById(R.id.etCode3);
        codeInputs[3] = findViewById(R.id.etCode4);
        btnVerify = findViewById(R.id.btnVerify);
        tvEmail = findViewById(R.id.tvEmail);

        tvEmail.setText(email);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.tvResend).setOnClickListener(v -> resendCode());
    }

    private void setupCodeInputs() {
        for (int i = 0; i < codeInputs.length; i++) {
            final int index = i;
            codeInputs[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < codeInputs.length - 1) {
                        codeInputs[index + 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    checkCodeComplete();
                }
            });
        }
    }

    private void setupListeners() {
        btnVerify.setOnClickListener(v -> {
            if (validateCode()) {
                // TODO: Implement actual code verification
                startActivity(new Intent(this, NewPasswordActivity.class));
            }
        });
    }

    private boolean validateCode() {
        StringBuilder code = new StringBuilder();
        for (EditText input : codeInputs) {
            String digit = input.getText().toString();
            if (digit.isEmpty()) {
                return false;
            }
            code.append(digit);
        }
        return code.length() == 4;
    }

    private void checkCodeComplete() {
        boolean isComplete = true;
        for (EditText input : codeInputs) {
            if (input.getText().toString().isEmpty()) {
                isComplete = false;
                break;
            }
        }
        btnVerify.setEnabled(isComplete);
    }

    private void resendCode() {
        // TODO: Implement code resend functionality
        for (EditText input : codeInputs) {
            input.setText("");
        }
        codeInputs[0].requestFocus();
    }
}
