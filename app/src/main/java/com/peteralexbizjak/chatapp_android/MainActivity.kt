package com.peteralexbizjak.chatapp_android

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.peteralexbizjak.chatapp_android.activities.auth.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Prepare Firebase stuff
        prepareFirebase()

        //Check if it is application's first run
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val state = sharedPreferences.getBoolean("firstrun", false)
        if (!state) {
            sharedPreferences.edit().putBoolean("firstrun", true).apply()
            startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
        }
    }

    private fun prepareFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference
    }
}
