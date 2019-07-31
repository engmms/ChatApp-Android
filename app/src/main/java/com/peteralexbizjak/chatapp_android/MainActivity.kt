package com.peteralexbizjak.chatapp_android

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.peteralexbizjak.chatapp_android.activities.auth.WelcomeActivity
import com.peteralexbizjak.chatapp_android.activities.channel.ChatActivity
import com.peteralexbizjak.chatapp_android.activities.channel.FindUsers
import com.peteralexbizjak.chatapp_android.adapters.ChannelRecyclerAdapter
import com.peteralexbizjak.chatapp_android.interfaces.OnItemClickListener
import com.peteralexbizjak.chatapp_android.models.firestore.ChatModel

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseFirestore: FirebaseFirestore

    private lateinit var toolbar: RelativeLayout

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChannelRecyclerAdapter
    private var chatModelList: MutableList<ChatModel> = ArrayList()

    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Check if it is application's first run
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val state = sharedPreferences.getBoolean("firstrun", false)
        if (!state) {
            sharedPreferences.edit().putBoolean("firstrun", true).apply()
            startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
        } else {
            prepareFirebase()
            initializeViews()
        }
    }

    private fun prepareFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference
        firebaseFirestore = FirebaseFirestore.getInstance()
    }

    private fun initializeViews() {
        toolbar = findViewById(R.id.mainToolbar)

        recyclerView = findViewById(R.id.mainRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        firebaseFirestore.collection("chats").get().addOnCompleteListener {
            Log.d(TAG, "Firestore is being queried...")
            if (it.isSuccessful) {
                Log.d(TAG, "...query is successful")

                //TODO: discriminate between chats user has access to!!!!!!!!!
                for (documentSnapshot: DocumentSnapshot in it.result!!.documents)
                    documentSnapshot.toObject(ChatModel::class.java)?.let { chatModelObject ->
                        chatModelList.clear()
                        if (chatModelObject.chatId.contains(firebaseAuth.currentUser!!.uid)) chatModelList.add(chatModelObject)
                    }

                adapter = ChannelRecyclerAdapter(this@MainActivity, chatModelList, firebaseAuth.currentUser!!.uid)
                recyclerView.adapter = adapter

                adapter.notifyDataSetChanged()
            }
        }

        recyclerView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(view: View, position: Int) {
                val chatModel: ChatModel = chatModelList[position]
                val chatId: String = chatModel.chatId

                chatModel.participants.forEach {
                    if (!it.keys.contains(firebaseAuth.currentUser!!.uid)) {
                        val recipientId: String = it.keys.elementAt(0)
                        it.values.forEach { participantModel ->
                            val recipientDisplayName: String = participantModel.displayName
                            val recipientPhotoUrl: String = participantModel.displayName

                            val intent = Intent(this@MainActivity, ChatActivity::class.java)
                            intent.putExtra("recipientId", recipientId)
                            intent.putExtra("recipientDisplayName", recipientDisplayName)
                            intent.putExtra("recipientPhotoUrl", recipientPhotoUrl)
                            intent.putExtra("chatId", chatId)
                            startActivity(intent)
                        }
                    }
                }
            }
        })

        floatingActionButton = findViewById(R.id.mainFab)
        floatingActionButton.setOnClickListener { startActivity(Intent(this@MainActivity, FindUsers::class.java)) }
    }

    private fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object: RecyclerView.OnChildAttachStateChangeListener {

            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(view, holder.adapterPosition)
                }
            }
        })
    }

    companion object {
        private const val TAG: String = "main-activity"
    }
}
