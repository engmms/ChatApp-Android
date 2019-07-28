package com.peteralexbizjak.chatapp_android.activities.channel

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.peteralexbizjak.chatapp_android.R

class ChatActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
    }

    companion object {
        private val nonEmptyColor: Int = Color.parseColor("#f44336")
        private val emptyColor: Int = Color.parseColor("#666666")
    }
}