package com.peteralexbizjak.chatappandroid.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.peteralexbizjak.chatappandroid.R;
import com.peteralexbizjak.chatappandroid.models.ChannelModel;
import com.peteralexbizjak.chatappandroid.models.MessageModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        //Set on click listener to sendMessageIcon
        sendMessageIconClickListener();
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

    private void sendMessageIconClickListener() {
        sendMessageIcon.setOnClickListener(v -> {
            if (!messageEditText.getText().toString().isEmpty()) {
                String channelId, messageId;
                List<String> participants = new ArrayList<>();

                //Generate channel and message IDs
                channelId = databaseReference.push().getKey();
                messageId = databaseReference.push().getKey();

                //Acquire participant IDs and add them to the list (first the current user, then the recipient)
                participants.add(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
                participants.add(recipientId);

                //Get message text
                String messageText = messageEditText.getText().toString();

                if (participants.size() == 2 && channelId != null && messageId != null) {

                    //Write to database both channel and message
                    databaseReference.child(channelId).setValue(new ChannelModel(channelId, participants));
                    databaseReference.child(messageId).setValue(new MessageModel(messageId, recipientId, messageText));
                } else Toast.makeText(this, "Error creating channel", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(this, "Cannot send empty text", Toast.LENGTH_SHORT).show();
        });
    }
}
