package com.peteralexbizjak.chatapp_android.activities.navigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.peteralexbizjak.chatapp_android.MainActivity
import com.peteralexbizjak.chatapp_android.R
import com.peteralexbizjak.chatapp_android.models.general.UserModel
import com.peteralexbizjak.chatapp_android.utils.CircleTransform
import com.squareup.picasso.Picasso

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
        populateAccountData()
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
        supportActionBar?.setDisplayShowTitleEnabled(false)

        profilePic = findViewById(R.id.accountProfileImage)
        profileDisplayName = findViewById(R.id.accountProfileDisplayName)
        profileEmailPhoneNumber = findViewById(R.id.accountProfileEmailPhone)

        editAccount = findViewById(R.id.accountEditAccountButton)
        editAccount.setOnClickListener { startActivity(Intent(this@AccountActivity, EditAccountActivity::class.java)) }
    }

    private fun populateAccountData() {

        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    if (snapshot.getValue(UserModel::class.java)?.uid == firebaseAuth.currentUser!!.uid) {
                        val userModel: UserModel? = snapshot.getValue(UserModel::class.java)

                        if (userModel != null) {
                            if (userModel.photoUrl != null) Picasso.get().load(userModel.photoUrl).transform(CircleTransform()).into(profilePic)
                            profileDisplayName.text = userModel.displayName

                            when {
                                userModel.phoneNumber != null -> profileEmailPhoneNumber.text = "${userModel.email}\n${userModel.phoneNumber}"
                                else -> profileEmailPhoneNumber.text = userModel.email
                            }
                        }

                        break
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AccountActivity, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}