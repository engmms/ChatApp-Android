package com.peteralexbizjak.chatapp_android.activities.auth

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
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
import com.peteralexbizjak.chatapp_android.MainActivity
import com.peteralexbizjak.chatapp_android.R
import com.peteralexbizjak.chatapp_android.models.general.UserModel
import com.peteralexbizjak.chatapp_android.utils.PermissionHelper

class WelcomeActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var login: MaterialButton

    private lateinit var signUp: TextView
    private lateinit var forgotPassword: TextView

    private lateinit var googleSignIn: SignInButton
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInOptions: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        //Request runtime permissions
        PermissionHelper.requestRuntimePermissions(this)

        //Prepare Firebase stuff
        prepareFirebase()

        //Initialize views
        initializeViews()

        //Prepare Google sign in process
        googleSignIn.setOnClickListener { googleSignIn() }

        //Prepare password-based sign in process
        login.setOnClickListener { passwordSignIn() }

        //Transition to SignUpActivity
        signUp.setOnClickListener { startActivity(Intent(this@WelcomeActivity, SignUpActivity::class.java)) }
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
        startActivityForResult(googleSignInClient.signInIntent,
            googleRequestCode
        )
    }

    private fun passwordSignIn() {
        val emailString: String = email.text.toString()
        val passwordString: String = password.text.toString()
        if (emailString.isNotEmpty() && passwordString.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener {
                    if (it.isSuccessful) startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                }
                .addOnFailureListener {
                    displayCustomToast("Could not authenticate ${it.message}")
                }
        } else displayCustomToast("Cannot have empty email or password!")
    }

    private fun authenticateWithGoogle(account: GoogleSignInAccount?) {
        if (account != null) {
            val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener{
                if (it.isSuccessful) {
                    val user: FirebaseUser = it.result!!.user
                    databaseReference.child(user.uid).setValue(
                        UserModel(
                            user.uid,
                            user.displayName,
                            user.email,
                            user.photoUrl.toString()
                        )
                    )
                    startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                }
            }
        }
    }

    private fun prepareFirebase() {
        //Get instance of FirebaseAuth object
        firebaseAuth = FirebaseAuth.getInstance()

        //Get instance of FirebaseDatabase and DatabaseReference
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        //Configure Google Sign In process
        configureGoogleSignIn()
    }

    private fun initializeViews() {
        email = findViewById(R.id.welcomeEmailInput)
        password = findViewById(R.id.welcomePasswordInput)
        login = findViewById(R.id.welcomeLoginButton)

        signUp = findViewById(R.id.welcomeSignUp)
        forgotPassword = findViewById(R.id.welcomeForgotPassword)

        googleSignIn = findViewById(R.id.welcomeGoogleSignIn)
    }

    private fun displayCustomToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val googleRequestCode: Int = 729
    }
}