package com.peteralexbizjak.chatappandroid

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.peteralexbizjak.chatappandroid.activities.SignInActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Check if it's application's first run
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val state = sharedPreferences.getBoolean("firstrun", false)
        if (!state) {
            sharedPreferences.edit().putBoolean("firstrun", true).apply()
            startActivity(Intent(this@MainActivity, SignInActivity::class.java))
        }

        //Initialize views
        recyclerView = findViewById(R.id.mainRecyclerView)
    }
}
