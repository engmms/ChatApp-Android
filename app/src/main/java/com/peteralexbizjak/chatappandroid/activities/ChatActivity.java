package com.peteralexbizjak.chatappandroid.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.peteralexbizjak.chatappandroid.MainActivity;
import com.peteralexbizjak.chatappandroid.R;
import com.peteralexbizjak.chatappandroid.adapters.MessageRecyclerAdapter;
import com.peteralexbizjak.chatappandroid.models.ChannelModel;
import com.peteralexbizjak.chatappandroid.models.MessageModel;
import com.peteralexbizjak.chatappandroid.utils.RandomColorGenerator;

import java.util.ArrayList;
import java.util.HashMap;
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

    private String recipientId, recipientDisplayName, recipientPhotoUrl;
    private String channelIdGlobal;

    List<MessageModel> messageModelList = new ArrayList<>();
    List<HashMap<String, String>> personProfilUrlHash = new ArrayList<>();

    MessageRecyclerAdapter adapter = new MessageRecyclerAdapter(this, messageModelList, personProfilUrlHash);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chate);

        recipientId = getIntent().getStringExtra("recipientId");
        recipientDisplayName = getIntent().getStringExtra("recipientDisplayName");
        recipientPhotoUrl = getIntent().getStringExtra("recipientPhotoUrl");

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
        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(ChatActivity.this, MainActivity.class)));

        //Setup chat area
        setupChatArea();

        //Set on click listener to sendMessageIcon
        sendMessageIconClickListener();
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private List<MessageModel> displayChatMessages() {
        databaseReference.child(channelIdGlobal).child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageModelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MessageModel messageModel = snapshot.getValue(MessageModel.class);
                    if (messageModel != null) messageModelList.add(messageModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return messageModelList;
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
            Toast.makeText(this, "Send message", Toast.LENGTH_SHORT).show();
            if (!messageEditText.getText().toString().isEmpty()) {
                String channelId, messageId;

                if (channelIdGlobal == null) {
                    //Generate channel ID and set global channel ID
                    channelId = databaseReference.push().getKey();
                    channelIdGlobal = channelId;

                    //Generate message ID and set
                    messageId = databaseReference.push().getKey();

                    setupRecyclerView();

                    //Generate hash map of reduced current user data
                    HashMap<String, List<String>> currentUserReducedData = new HashMap<>();
                    List<String> currentUserReduced = new ArrayList<>();
                    currentUserReduced.add(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName());
                    currentUserReduced.add(Objects.requireNonNull(firebaseAuth.getCurrentUser().getPhotoUrl()).toString());
                    currentUserReducedData.put(firebaseAuth.getCurrentUser().getUid(), currentUserReduced);

                    //Generate hash map of reduced recipient data
                    HashMap<String, List<String>> recipientReducedData = new HashMap<>();
                    List<String> recipientReduced = new ArrayList<>();
                    recipientReduced.add(recipientDisplayName);
                    recipientReduced.add(recipientPhotoUrl);
                    recipientReducedData.put(recipientId, recipientReduced);

                    //Generate a list of reduced users data
                    List<HashMap<String, List<String>>> listOfReducedData = new ArrayList<>();
                    listOfReducedData.add(currentUserReducedData);
                    listOfReducedData.add(recipientReducedData);

                    //Generate a hash map where key is the user ID and value is profile picture URL
                    HashMap<String, String> currentUserIdPhoto = new HashMap<>();
                    HashMap<String, String> recipientIdPhoto = new HashMap<>();
                    currentUserIdPhoto.put(firebaseAuth.getCurrentUser().getUid(), firebaseAuth.getCurrentUser().getPhotoUrl().toString());
                    recipientIdPhoto.put(recipientId, recipientPhotoUrl);

                    //Add the whole thing to the personProfilUrlHash
                    personProfilUrlHash.clear();
                    personProfilUrlHash.add(currentUserIdPhoto);
                    personProfilUrlHash.add(recipientIdPhoto);

                    //Get message text
                    String messageText = messageEditText.getText().toString();

                    if (messageId != null) {

                        //Write to database both channel and message
                        databaseReference.child(channelIdGlobal).setValue(new ChannelModel(
                                channelId, listOfReducedData, RandomColorGenerator.generateRandomColorInt()
                        ));
                        databaseReference.child(channelIdGlobal).child("chat").child(messageId).setValue(new MessageModel(
                                messageId, Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid(), messageText
                        ));
                    } else Toast.makeText(this, "Error creating channel", Toast.LENGTH_SHORT).show();
                } else {
                    //Generate message ID and set
                    messageId = databaseReference.push().getKey();

                    setupRecyclerView();

                    //Get message text
                    String messageText = messageEditText.getText().toString();

                    if (messageId != null) {

                        //Write to database both channel and message
                        databaseReference.child(channelIdGlobal).child("chat").child(messageId).setValue(new MessageModel(
                                messageId, Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid(), messageText
                        ));
                    } else Toast.makeText(this, "Error creating channel", Toast.LENGTH_SHORT).show();
                }

                displayChatMessages();
            } else Toast.makeText(this, "Cannot send empty text", Toast.LENGTH_SHORT).show();
        });
    }
}
