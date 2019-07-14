package com.peteralexbizjak.chatappandroid.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.peteralexbizjak.chatappandroid.MainActivity
import com.peteralexbizjak.chatappandroid.R

class FindUsers : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var searchIcon: ImageView
    private lateinit var searchField: EditText
    private lateinit var recyclerView: RecyclerView

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_users)

        //Initialize the views
        toolbar = findViewById(R.id.findUserToolbar)
        searchIcon = findViewById(R.id.findUserSearchIcon)
        searchField = findViewById(R.id.findUserSearchField)
        recyclerView = findViewById(R.id.findUserRecyclerView)

        //Setup Toolbar
        setSupportActionBar(toolbar)

        //Set navigation icon click listener
        toolbar.setNavigationOnClickListener { startActivity(Intent(this@FindUsers, MainActivity::class.java)) }

        //Get instance of FirebaseAuth object
        firebaseAuth = FirebaseAuth.getInstance()

        //Get instance of FirebaseDatabase and DatabaseReference
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")
    }
}
