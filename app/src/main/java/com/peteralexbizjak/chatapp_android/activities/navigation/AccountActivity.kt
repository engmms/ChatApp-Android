package com.peteralexbizjak.chatapp_android.activities.navigation

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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

        prepareFirebase()
        initializeViews()
    }

    private fun prepareFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")
    }
}