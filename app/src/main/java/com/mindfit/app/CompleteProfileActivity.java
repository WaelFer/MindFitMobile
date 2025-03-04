package com.mindfit.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CompleteProfileActivity extends AppCompatActivity {
    private TextInputLayout tilFullName, tilAge, tilState;
    private TextInputEditText etFullName, etAge;
    private AutoCompleteTextView actvState;
    private MaterialButton btnComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        initViews();
        setupStateDropdown();
        setupListeners();
    }

    private void initViews() {
        tilFullName = findViewById(R.id.tilFullName);
        tilAge = findViewById(R.id.tilAge);
        tilState = findViewById(R.id.tilState);
        etFullName = findViewById(R.id.etFullName);
        etAge = findViewById(R.id.etAge);
        actvState = findViewById(R.id.actvState);
        btnComplete = findViewById(R.id.btnComplete);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void setupStateDropdown() {
        String[] stats = new String[]{"Sedentic", "Actif", "Athletic"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            stats
        );
        actvState.setAdapter(adapter);
    }

    private void setupListeners() {
        btnComplete.setOnClickListener(v -> {
            if (validateInput()) {
                // TODO: Save profile information
                startActivity(new Intent(this, HomeActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
    }

    private boolean validateInput() {
        boolean isValid = true;
        String fullName = etFullName.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String state = actvState.getText().toString().trim();

        if (fullName.isEmpty()) {
            tilFullName.setError("Full name is required");
            isValid = false;
        } else {
            tilFullName.setError(null);
        }

        if (age.isEmpty()) {
            tilAge.setError("Age is required");
            isValid = false;
        } else {
            try {
                int ageValue = Integer.parseInt(age);
                if (ageValue < 13 || ageValue > 120) {
                    tilAge.setError("Please enter a valid age (13-120)");
                    isValid = false;
                } else {
                    tilAge.setError(null);
                }
            } catch (NumberFormatException e) {
                tilAge.setError("Please enter a valid age");
                isValid = false;
            }
        }

        if (state.isEmpty()) {
            tilState.setError("Please select your state");
            isValid = false;
        } else {
            tilState.setError(null);
        }

        return isValid;
    }
}
