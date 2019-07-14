package com.peteralexbizjak.chatappandroid.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.peteralexbizjak.chatappandroid.MainActivity
import com.peteralexbizjak.chatappandroid.R
import com.peteralexbizjak.chatappandroid.adapters.UserRecyclerAdapter
import com.peteralexbizjak.chatappandroid.interfaces.OnItemClickListener
import com.peteralexbizjak.chatappandroid.models.UserModel

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

        //Get instance of FirebaseAuth object
        firebaseAuth = FirebaseAuth.getInstance()

        //Get instance of FirebaseDatabase and DatabaseReference
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        //Initialize the views
        toolbar = findViewById(R.id.findUserToolbar)
        searchIcon = findViewById(R.id.findUserSearchIcon)
        searchField = findViewById(R.id.findUserSearchField)
        recyclerView = findViewById(R.id.findUserRecyclerView)

        //Set the layout manager for the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Setup Toolbar
        setSupportActionBar(toolbar)

        //Set navigation icon click listener
        toolbar.setNavigationOnClickListener { startActivity(Intent(this@FindUsers, MainActivity::class.java)) }

        //Start listening for changes to EditText
        setupEditTextChangeListener()
    }

    /**
     * Listener that follows changes when user edits EditText. Initiate query to the database only when user stops
     * typing and contents are not blank or null.
     */
    private fun setupEditTextChangeListener() {
        searchField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrBlank()) displayFoundUsers(searchForUsers(s.toString()))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    /**
     * Display found users in a RecyclerView with the help of a custom adapter and transition to ChatActivity with
     * recipient data (their uid and display name)
     */
    private fun displayFoundUsers(listOfUsers: MutableList<UserModel>) {
        val recyclerViewAdapter = UserRecyclerAdapter(this, listOfUsers)
        recyclerView.adapter = recyclerViewAdapter
        recyclerViewAdapter.notifyDataSetChanged()
        recyclerView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(view: View, position: Int) {
                val intent = Intent(this@FindUsers, ChatActivity::class.java)
                intent.putExtra("recipientId", listOfUsers[position].id)
                intent.putExtra("recipientDisplayName", listOfUsers[position].displayName)
                startActivity(intent)
            }
        })
    }

    /**
     * Search for users in the database that satisfy the search query, which can either represent display
     * name or email
     */
    private fun searchForUsers(searchQuery: String): MutableList<UserModel> {

        //Declare and instantiate a MutableList of UserModel objects
        val userModelList: MutableList<UserModel> = ArrayList()

        //Transform searchQuery into lowercase
        searchQuery.toLowerCase()

        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FindUsers, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val user: UserModel = snapshot.getValue(UserModel::class.java)!!
                    if (user.id != firebaseAuth.currentUser!!.uid && (user.displayName.toLowerCase().contains(searchQuery) || user.email.toLowerCase().contains(searchQuery)))
                        userModelList.add(user)
                }
            }
        })

        return userModelList
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


}
