package com.peteralexbizjak.chatapp_android.activities.channel

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.peteralexbizjak.chatapp_android.R

class ChatActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendMessageIcon: ImageView

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var recipientId: String
    private lateinit var recipientDisplayName: String
    private lateinit var recipientPhotoUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //Get recipient details from the intent
        recipientId = intent.getStringExtra("recipientId")
        recipientDisplayName = intent.getStringExtra("recipientDisplayName")
        recipientPhotoUrl = intent.getStringExtra("recipientPhotoUrl")

        //Prepare Firebase and initialize views
        prepareFirebase()
        initializeViews()
    }

    private fun prepareFirebase() {

    }

    private fun initializeViews() {
        toolbar = findViewById(R.id.chatToolbar)
        setSupportActionBar(toolbar)
        toolbar.title = recipientDisplayName
        toolbar.setNavigationOnClickListener { startActivity(Intent(this@ChatActivity, FindUsers::class.java)) }

        recyclerView = findViewById(R.id.chatRecyclerView)
        messageEditText = findViewById(R.id.chatEditText)
        sendMessageIcon = findViewById(R.id.chatSendMessage)

        messageEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(messageText: CharSequence?, start: Int, before: Int, count: Int) {
                if (messageText.toString().isEmpty()) sendMessageIcon.setColorFilter(emptyColor)
                else sendMessageIcon.setColorFilter(nonEmptyColor)
            }
        })
    }

    companion object {
        private val nonEmptyColor: Int = Color.parseColor("#f44336")
        private val emptyColor: Int = Color.parseColor("#666666")
    }
}