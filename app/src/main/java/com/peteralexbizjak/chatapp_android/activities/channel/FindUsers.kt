package com.peteralexbizjak.chatapp_android.activities.channel

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
import com.peteralexbizjak.chatapp_android.MainActivity
import com.peteralexbizjak.chatapp_android.R
import com.peteralexbizjak.chatapp_android.adapters.UserRecyclerAdapter
import com.peteralexbizjak.chatapp_android.interfaces.OnItemClickListener
import com.peteralexbizjak.chatapp_android.models.UserModel

class FindUsers : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var searchIcon: ImageView
    private lateinit var searchField: EditText
    private lateinit var recyclerView: RecyclerView

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var recyclerViewAdapter: UserRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_users)

        //Prepare Firebase and initialize views
        prepareFirebase()
        initializeViews()
    }

    private fun prepareFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")
    }

    private fun initializeViews() {
        toolbar = findViewById(R.id.findUserToolbar)
        toolbar.setNavigationOnClickListener { startActivity(Intent(this@FindUsers, MainActivity::class.java)) }
        setSupportActionBar(toolbar)

        searchIcon = findViewById(R.id.findUserSearchIcon)
        searchField = findViewById(R.id.findUserSearchField)

        recyclerView = findViewById(R.id.findUserRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setupEditTextChangeListener()
    }

    private fun setupEditTextChangeListener() {
        searchField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrBlank()) displayFoundUsers(searchForUsers(s.toString()))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun displayFoundUsers(listOfUsers: MutableList<UserModel>) {
        val recyclerViewAdapter = UserRecyclerAdapter(this, listOfUsers)
        recyclerView.adapter = recyclerViewAdapter
        recyclerViewAdapter.notifyDataSetChanged()
        recyclerView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(view: View, position: Int) {
                val intent = Intent(this@FindUsers, ChatActivity::class.java)
                intent.putExtra("recipientId", listOfUsers[position].uid)
                intent.putExtra("recipientDisplayName", listOfUsers[position].displayName)
                intent.putExtra("recipientPhotoUrl", listOfUsers[position].photoUrl)
                startActivity(intent)
            }
        })
    }

    private fun searchForUsers(searchQuery: String): MutableList<UserModel> {
        val userModelList: MutableList<UserModel> = ArrayList()

        searchQuery.toLowerCase()

        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FindUsers, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    val user: UserModel = snapshot.getValue(UserModel::class.java)!!
                    if (
                        user.uid != firebaseAuth.currentUser!!.uid &&
                        (
                            user.displayName.toLowerCase().contains(searchQuery) ||
                            user.email.toLowerCase().contains(searchQuery)
                        )
                    ) userModelList.add(user)
                }
            }
        })

        return userModelList
    }

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