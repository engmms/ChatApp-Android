package com.peteralexbizjak.chatappandroid

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.peteralexbizjak.chatappandroid.activities.ChatActivity
import com.peteralexbizjak.chatappandroid.activities.FindUsers
import com.peteralexbizjak.chatappandroid.activities.auth.SignInActivity
import com.peteralexbizjak.chatappandroid.adapters.ChannelRecyclerAdapter
import com.peteralexbizjak.chatappandroid.interfaces.OnItemClickListener
import com.peteralexbizjak.chatappandroid.models.ChannelModel

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private var listOfChannels: MutableList<ChannelModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get instance of FirebaseAuth object
        firebaseAuth = FirebaseAuth.getInstance()

        //Get instance of FirebaseDatabase and DatabaseReference
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("channels")

        //Check if it's application's first run
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val state = sharedPreferences.getBoolean("firstrun", false)
        if (!state) {
            sharedPreferences.edit().putBoolean("firstrun", true).apply()
            startActivity(Intent(this@MainActivity, SignInActivity::class.java))
        }

        //Initialize views
        floatingActionButton = findViewById(R.id.mainFab)
        recyclerView = findViewById(R.id.mainRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Display all channels in which a user participates
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    val channel: ChannelModel? = snapshot.getValue(ChannelModel::class.java)
                    channel!!.basicUserInfos.forEach {
                        for (key: String in it.keys) {
                            if (key == firebaseAuth.currentUser?.uid) listOfChannels.add(channel)
                            break
                        }
                    }
                }

                //Display channels in a RecyclerView
                displayChannels()
            }
        })

        //Set on click listener for FloatingActionButton
        floatingActionButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, FindUsers::class.java))
        }
    }

    private fun displayChannels() {
        val channelRecyclerAdapter = ChannelRecyclerAdapter(this, listOfChannels)
        recyclerView.adapter = channelRecyclerAdapter
        channelRecyclerAdapter.notifyDataSetChanged()
        recyclerView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(view: View, position: Int) {
                val intent = Intent(this@MainActivity, ChatActivity::class.java)
                val participantData: List<String> = extractRecipientData(listOfChannels[position])
            }
        })
    }

    /**
     * Setup on click listener for RecyclerView
     */
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

    /**
     * Return a list of recipient data relevant to the transition from the main activity to the chat activity. This list
     * contains three items in the precisely defined order.
     *
     * Index 0: recipient ID
     * Index 1: recipient display name
     * Index 2: recipient profile photo URL (as a string)
     */
    private fun extractRecipientData(channelModel: ChannelModel): List<String> {
        var listOfRecipientData: List<String> = emptyList()
        channelModel.basicUserInfos?.forEach {
            if (!it.containsKey(firebaseAuth.currentUser?.uid)) {
                val recipientId: String = it.keys.elementAt(0)
                listOfRecipientData = listOf(recipientId, it[recipientId]!![0], it[recipientId]!![1])
            }
        }
        return listOfRecipientData
    }
}
