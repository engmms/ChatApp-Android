package com.peteralexbizjak.chatappandroid.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.peteralexbizjak.chatappandroid.R;

public class ChatActivity extends AppCompatActivity {

    private static final int nonEmptyColor = Color.parseColor("#4bacb8");
    private static final int emptyColor = Color.parseColor("#666666");

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private EditText messageEditText;
    private ImageView sendMessageIcon;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String recipientId, recipientDisplayName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chate);

        recipientId = getIntent().getStringExtra("recipientId");
        recipientDisplayName = getIntent().getStringExtra("recipientDisplayName");

        //Get instance of FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //Get instance of FirebaseDatabase and DatabaseReference
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("channels");

        //Initialize views
        toolbar = findViewById(R.id.chatToolbar);
        recyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.chatEditText);
        sendMessageIcon = findViewById(R.id.chatSendMessage);

        //Setup Toolbar
        setSupportActionBar(toolbar);
        toolbar.setTitle(recipientDisplayName);

        //Setup chat area
        setupChatArea();
    }

    private void setupChatArea() {
        messageEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence messageText, int start, int before, int count) {
                //Change color of the sendMessageIcon according to detected text changes
                sendMessageIcon.setColorFilter(messageText.toString().isEmpty() ? emptyColor : nonEmptyColor);
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
    }
}
