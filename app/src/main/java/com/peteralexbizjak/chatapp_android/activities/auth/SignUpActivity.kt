package com.peteralexbizjak.chatapp_android.activities.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.peteralexbizjak.chatapp_android.utils.ValidatorHelper
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.peteralexbizjak.chatapp_android.MainActivity
import com.peteralexbizjak.chatapp_android.R
import com.peteralexbizjak.chatapp_android.models.UserModel


class SignUpActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var toolbar: Toolbar

    private lateinit var displayName: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var retypedPassword: TextInputEditText

    private lateinit var finish: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //Prepare Firebase stuff
        prepareFirebase()

        //Initialize views
        initializeViews()
    }

    private fun prepareFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")
    }

    private fun initializeViews() {
        toolbar = findViewById(R.id.signupToolbar)
        toolbar.setNavigationOnClickListener { startActivity(Intent(this@SignUpActivity, WelcomeActivity::class.java)) }
        setSupportActionBar(toolbar)

        displayName = findViewById(R.id.signupUsernameInput)
        email = findViewById(R.id.signupEmailInput)
        password = findViewById(R.id.signupPasswordInput)
        retypedPassword = findViewById(R.id.signupRetypePasswordInput)

        finish = findViewById(R.id.signupButton)
        finish.setOnClickListener { createNewAccount() }
    }

    private fun createNewAccount() {
        val validatorHelper = ValidatorHelper()
        if (validatorHelper.isEmailValid(emailString = email.text.toString())) {
            if (validatorHelper.doPasswordsMatch(passwordString = password.text.toString(), retypedPassword = retypedPassword.text.toString())) {
                if (displayName.text.toString().isNotEmpty()) {
                    firebaseAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                        .addOnCompleteListener {
                            val firebaseUser: FirebaseUser = it.result!!.user
                            databaseReference
                                .child(firebaseUser.uid)
                                .setValue(UserModel(
                                    firebaseUser.uid,
                                    displayName.text.toString(),
                                    email.text.toString(),
                                    null
                                ))
                            startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                        }
                } else displayCustomToast("Display name cannot be empty")
            } else displayCustomToast("Passwords do not match")
        } else displayCustomToast("Email is in improper format")
    }

    private fun displayCustomToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}