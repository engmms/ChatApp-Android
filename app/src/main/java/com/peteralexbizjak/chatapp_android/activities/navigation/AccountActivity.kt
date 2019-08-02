package com.peteralexbizjak.chatapp_android.activities.navigation

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.peteralexbizjak.chatapp_android.MainActivity
import com.peteralexbizjak.chatapp_android.R

class AccountActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    private lateinit var profilePic: ImageView
    private lateinit var profileDisplayName: TextView
    private lateinit var profileEmailPhoneNumber: TextView

    private lateinit var editAccount: MaterialButton

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        prepareFirebase()
        initializeViews()
    }

    private fun prepareFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")
    }

    private fun initializeViews() {
        toolbar = findViewById(R.id.accountToolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { startActivity(Intent(this@AccountActivity, MainActivity::class.java)) }

        profilePic = findViewById(R.id.accountProfileImage)
        profileDisplayName = findViewById(R.id.accountProfileDisplayName)
        profileEmailPhoneNumber = findViewById(R.id.accountProfileEmailPhone)

        editAccount = findViewById(R.id.accountEditAccountButton)
        editAccount.setOnClickListener { startActivity(Intent(this@AccountActivity, EditAccountActivity::class.java)) }
    }
}