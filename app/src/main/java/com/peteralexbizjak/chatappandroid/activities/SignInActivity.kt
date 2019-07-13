package com.peteralexbizjak.chatappandroid.activities

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.peteralexbizjak.chatappandroid.MainActivity
import com.peteralexbizjak.chatappandroid.R
import com.peteralexbizjak.chatappandroid.models.UserModel
import com.peteralexbizjak.chatappandroid.utils.PermissionHelper
import com.peteralexbizjak.chatappandroid.utils.ValidatorHelper

class SignInActivity: AppCompatActivity() {

    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var login: MaterialButton
    private lateinit var signInButton: SignInButton
    private lateinit var createNewAccount: LinearLayout

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInOptions: GoogleSignInOptions

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_sign_in)

        //Check for camera and external storage permissions
        PermissionHelper.requestRuntimePermissions(this)

        //Initialize all views
        email = findViewById(R.id.welcomeActivityEmail)
        password = findViewById(R.id.welcomeActivityPassword)
        login = findViewById(R.id.welcomeActivityLoginButton)
        signInButton = findViewById(R.id.welcomeActivityGoogleSignIn)
        createNewAccount = findViewById(R.id.welcomeActivityContainerNoAccount)

        //Configure Google Sign In here
        configureGoogleSignIn()

        //Get instance of FirebaseAuth object
        firebaseAuth = FirebaseAuth.getInstance()

        //Get instance of FirebaseDatabase and DatabaseReference
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        //Setup SignInButton on click listener
        signInButton.setOnClickListener { googleSignIn() }

        //Setup MaterialButton that initiates password-based authentication
        login.setOnClickListener{ passwordSignIn() }

        //Transition to CreateAccountActivity
        createNewAccount.setOnClickListener { startActivity(Intent(this@SignInActivity, CreateAccountActivity::class.java)) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == googleRequestCode) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                authenticateWithGoogle(account)
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }

    private fun configureGoogleSignIn() {
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun googleSignIn() {
        startActivityForResult(googleSignInClient.signInIntent, googleRequestCode)
    }

    private fun passwordSignIn() {
        val validatorHelper = ValidatorHelper()
        val emailString = email.text.toString()
        val passwordString = validatorHelper.hashPassword(password.text.toString())
        if (emailString.isNotEmpty() && passwordString!!.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener {
                    if (it.isSuccessful) startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Could not authenticate: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else Toast.makeText(this, "Email and password fields cannot be empty", Toast.LENGTH_SHORT).show()
    }

    private fun authenticateWithGoogle(account: GoogleSignInAccount?) {
        if (account != null) {
            val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener{
                if (it.isSuccessful) {
                    val user: FirebaseUser = it.result!!.user
                    databaseReference.child(user.uid).setValue(UserModel(user.uid, user.displayName, user.email, user.photoUrl.toString()))
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                }
            }
        }
    }

    companion object {
        private const val googleRequestCode: Int = 729
    }
}
