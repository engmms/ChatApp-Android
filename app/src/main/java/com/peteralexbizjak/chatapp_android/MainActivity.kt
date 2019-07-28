package com.peteralexbizjak.chatapp_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.peteralexbizjak.chatapp_android.activities.WelcomeActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO: temporary transition, fix later
        startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
    }
}
