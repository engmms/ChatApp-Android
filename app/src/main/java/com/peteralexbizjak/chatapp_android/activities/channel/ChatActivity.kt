package com.peteralexbizjak.chatapp_android.activities.channel

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.ChildEventRegistration
import com.google.firebase.firestore.*
import com.peteralexbizjak.chatapp_android.MainActivity
import com.peteralexbizjak.chatapp_android.R
import com.peteralexbizjak.chatapp_android.adapters.MessageRecyclerAdapter
import com.peteralexbizjak.chatapp_android.models.firestore.ChatModel
import com.peteralexbizjak.chatapp_android.models.firestore.MessageModel
import com.peteralexbizjak.chatapp_android.models.firestore.ParticipantModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendMessageIcon: ImageView

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

    private lateinit var recipientId: String
    private lateinit var recipientDisplayName: String
    private lateinit var recipientPhotoUrl: String

    private var chatId: String? = null
    private var latestMessageId: String? = null

    private lateinit var adapter: MessageRecyclerAdapter
    private lateinit var messageModelList: MutableList<MessageModel>

    private var registration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //Get recipient details from the intent
        recipientId = intent.getStringExtra("recipientId")
        recipientDisplayName = intent.getStringExtra("recipientDisplayName")
        recipientPhotoUrl = intent.getStringExtra("recipientPhotoUrl")
        chatId = intent.getStringExtra("chatId")

        //Prepare Firebase and initialize views
        prepareFirebase()
        initializeViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        registration?.remove()
    }

    private fun prepareFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
    }

    private fun initializeViews() {
        toolbar = findViewById(R.id.chatToolbar)
        setSupportActionBar(toolbar)
        toolbar.title = recipientDisplayName
        toolbar.setNavigationOnClickListener { startActivity(Intent(this@ChatActivity, MainActivity::class.java)) }

        recyclerView = findViewById(R.id.chatRecyclerView)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager
        if (chatId != null) {
            messageModelList = ArrayList()
            adapter = MessageRecyclerAdapter(this@ChatActivity, messageModelList)
            recyclerView.adapter = adapter
            fetchMessages()
        }

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

        sendMessageIcon.setOnClickListener { sendMessage(messageEditText.text.toString()) }
    }

    private fun sendMessage(message: String) {
        if (chatId == null) {
            //Handle creating current user structure
            val currentUserId: String? = firebaseAuth.currentUser!!.uid
            val currenctUserAsParticipant = ParticipantModel(
                firebaseAuth.currentUser!!.displayName,
                firebaseAuth.currentUser!!.photoUrl.toString()
            )
            val currentUserAsHashMap: HashMap<String, ParticipantModel> = HashMap()
            currentUserAsHashMap[currentUserId!!] = currenctUserAsParticipant

            //Handle creating recipient user structure
            val recipientUserAsParticipant = ParticipantModel(
                recipientDisplayName,
                recipientPhotoUrl
            )
            val recipientUserAsHashMap: HashMap<String, ParticipantModel> = HashMap()
            recipientUserAsHashMap[recipientId] = recipientUserAsParticipant

            //Create a list of HashMaps
            val hashmapsList: List<HashMap<String, ParticipantModel>> = arrayListOf(currentUserAsHashMap, recipientUserAsHashMap)

            //For latest message hash (key: current user ID, value: message text)
            val latestMessageHashMap: HashMap<String, String> = HashMap()
            latestMessageHashMap[currentUserId] = message

            //Create a chat object and update chatId
            chatId = "$currentUserId???$recipientId"
            val chatModel = ChatModel(chatId!!, hashmapsList, latestMessageHashMap)

            //Add data to Firestore
            firebaseFirestore
                .collection("chats")
                .document(chatId!!)
                .set(chatModel)

            //Now create a MessageModel and store it to database as well
            val messageId: String = Date().time.toString()
            latestMessageId = messageId
            val messageModel =
                MessageModel(messageId, message, currentUserId, firebaseAuth.currentUser!!.photoUrl.toString())
            firebaseFirestore
                .collection("chats")
                .document(chatId!!)
                .collection("messages")
                .document(messageId)
                .set(messageModel)
                .addOnCompleteListener {
                    when {
                        it.isSuccessful -> Log.d(TAG, "Channel and message stored to database")
                        else -> Log.d(TAG, "Channel and message were NOT stored to the database")
                    }
                }

            //Initialize RecyclerView adapter and the ModelMessage list
            messageModelList = ArrayList()
            adapter = MessageRecyclerAdapter(this@ChatActivity, messageModelList)
            recyclerView.adapter = adapter
            fetchMessages()

            //Clear the EditText + listen for changes in the database
            messageEditText.text.clear()
        } else {
            //Simply create a MessageModel and store it to database
            val messageId: String = Date().time.toString()
            latestMessageId = messageId
            val messageModel = MessageModel(
                messageId,
                message,
                firebaseAuth.currentUser!!.uid,
                firebaseAuth.currentUser!!.photoUrl.toString()
            )
            firebaseFirestore
                .collection("chats")
                .document(chatId!!)
                .collection("messages")
                .document(messageId)
                .set(messageModel)
                .addOnCompleteListener {
                    when {
                        it.isSuccessful -> Log.d(TAG, "Message stored to database")
                        else -> Log.d(TAG, "Message was NOT stored to the database")
                    }
                }

            val latestMessageHashMap: HashMap<String, String> = HashMap()
            latestMessageHashMap[firebaseAuth.currentUser!!.uid] = message

            //Update current chat document
            firebaseFirestore
                .collection("chats")
                .document(chatId!!)
                .update("latestMessage", latestMessageHashMap)

            //Clear the EditText + listen for changes in the database
            messageEditText.text.clear()
        }
    }

    private fun fetchMessages() {
        val query: CollectionReference = firebaseFirestore
            .collection("chats")
            .document(chatId!!)
            .collection("messages")
        registration = query.addSnapshotListener { snapshot, exception ->
            if (exception != null) Log.d(TAG, "Exception occurred", exception)
            if (snapshot != null) {
                messageModelList.clear()
                for (document: DocumentSnapshot in snapshot.documents) {
                    val messageModel: MessageModel? = document.toObject(MessageModel::class.java)
                    messageModel?.let { messageModelList.add(it) }
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    companion object {
        private val nonEmptyColor: Int = Color.parseColor("#f44336")
        private val emptyColor: Int = Color.parseColor("#666666")
        private const val TAG = "chat-activity"
    }
}