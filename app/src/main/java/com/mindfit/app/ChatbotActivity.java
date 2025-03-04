package com.mindfit.app;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mindfit.app.adapters.ChatAdapter;
import com.mindfit.app.api.ApiClient;
import com.mindfit.app.models.ChatMessage;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatbotActivity extends AppCompatActivity {
    private RecyclerView rvChat;
    private EditText etMessage;
    private ImageButton btnSend;
    private ChatAdapter adapter;
    private List<ChatMessage> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        initViews();
        setupRecyclerView();
        addWelcomeMessage();
    }

    private void initViews() {
        rvChat = findViewById(R.id.rvChat);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        btnSend.setOnClickListener(v -> sendMessage());
    }

    private void setupRecyclerView() {
        adapter = new ChatAdapter(messages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(layoutManager);
        rvChat.setAdapter(adapter);
    }

    private void addWelcomeMessage() {
        ChatMessage welcome = new ChatMessage(
            "Hello! I'm your MindFit AI assistant. How can I help you today?",
            false
        );
        messages.add(welcome);
        adapter.notifyItemInserted(messages.size() - 1);
    }

    private void sendMessage() {
        String message = etMessage.getText().toString().trim();
        if (message.isEmpty()) return;

        // Add user message
        messages.add(new ChatMessage(message, true));
        adapter.notifyItemInserted(messages.size() - 1);
        rvChat.smoothScrollToPosition(messages.size() - 1);

        // Clear input
        etMessage.setText("");

        // Get bot response
        ApiClient.getInstance()
                .getChatbotResponse(message)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String botResponse = response.body();
                            messages.add(new ChatMessage(botResponse, false));
                            adapter.notifyItemInserted(messages.size() - 1);
                            rvChat.smoothScrollToPosition(messages.size() - 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(ChatbotActivity.this,
                            "Failed to get response: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
