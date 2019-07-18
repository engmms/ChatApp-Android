package com.peteralexbizjak.chatappandroid

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.peteralexbizjak.chatappandroid.activities.FindUsers
import com.peteralexbizjak.chatappandroid.activities.auth.SignInActivity
import com.peteralexbizjak.chatappandroid.adapters.ChannelRecyclerAdapter
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
        Log.d("main-activity", listOfChannels.toString())
        val channelRecyclerAdapter = ChannelRecyclerAdapter(this, listOfChannels)
        recyclerView.adapter = channelRecyclerAdapter
        channelRecyclerAdapter.notifyDataSetChanged()
    }
}
