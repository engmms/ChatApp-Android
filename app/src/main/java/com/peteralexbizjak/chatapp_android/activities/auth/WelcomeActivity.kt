package com.peteralexbizjak.chatapp_android.activities.auth

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.SignInButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.peteralexbizjak.chatapp_android.R

class WelcomeActivity : AppCompatActivity() {

    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var login: MaterialButton

    private lateinit var signUp: TextView
    private lateinit var forgotPassword: TextView

    private lateinit var googleSignIn: SignInButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        //Initialize views
        initializeViews()
    }

    private fun initializeViews() {
        email = findViewById(R.id.welcomeEmailInput)
        password = findViewById(R.id.welcomePasswordInput)
        login = findViewById(R.id.welcomeLoginButton)

        signUp = findViewById(R.id.welcomeSignUp)
        forgotPassword = findViewById(R.id.welcomeForgotPassword)

        googleSignIn = findViewById(R.id.welcomeGoogleSignIn)
    }
}