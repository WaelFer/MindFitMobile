package com.mindfit.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText etNom, etPrenom, etAge, etEtat, etPassword;
    private Button btnUpdateProfile;
    private RequestQueue requestQueue;
    private String token;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etAge = findViewById(R.id.etAge);
        etEtat = findViewById(R.id.etEtat);
        etPassword = findViewById(R.id.etPassword);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);

        requestQueue = Volley.newRequestQueue(this);
        token = getIntent().getStringExtra("token");
        userId = getIntent().getStringExtra("user_id");

        // Load user data when the activity starts
        loadUserData();

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void loadUserData() {
        String url = "http://your-api-url/meditant/" + userId;

        JsonObjectRequest request = new JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            response -> {
                try {
                    etNom.setText(response.getString("nom"));
                    etPrenom.setText(response.getString("prenom"));
                    etAge.setText(String.valueOf(response.getInt("age")));
                    etEtat.setText(response.getString("etat"));
                    // Password is not shown for security reasons
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Erreur lors du chargement des données", Toast.LENGTH_SHORT).show();
                }
            },
            error -> {
                Log.e("UpdateProfile", "Error loading data: " + error.getMessage());
                Toast.makeText(this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
            }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private void updateProfile() {
        String url = "http://your-api-url/meditant/modifierProfil";

        JSONObject body = new JSONObject();
        try {
            body.put("nom", etNom.getText().toString());
            body.put("prenom", etPrenom.getText().toString());
            body.put("age", Integer.parseInt(etAge.getText().toString()));
            body.put("etat", etEtat.getText().toString());
            body.put("password", etPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de la préparation des données", Toast.LENGTH_SHORT).show();
            return;
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez entrer un âge valide", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
            Request.Method.PUT,
            url,
            body,
            response -> {
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        Toast.makeText(UpdateProfileActivity.this, "Profil mis à jour avec succès!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UpdateProfileActivity.this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(UpdateProfileActivity.this, "Erreur de format de réponse", Toast.LENGTH_SHORT).show();
                }
            },
            error -> {
                Log.e("UpdateProfile", "Error: " + error.getMessage());
                Toast.makeText(UpdateProfileActivity.this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
            }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(request);
    }
}
