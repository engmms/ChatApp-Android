package com.peteralexbizjak.chatappandroid.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.peteralexbizjak.chatappandroid.MainActivity;
import com.peteralexbizjak.chatappandroid.R;
import com.peteralexbizjak.chatappandroid.models.UserModel;
import com.peteralexbizjak.chatappandroid.utils.ValidatorHelper;

import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText username, email, password, retypedPassword;
    private MaterialButton login;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_account);

        //Initialize views
        username = findViewById(R.id.newAccountUsername);
        email = findViewById(R.id.newAccountEmail);
        password = findViewById(R.id.newAccountPassword);
        retypedPassword = findViewById(R.id.newAccountRetypePassword);
        login = findViewById(R.id.newAccountLoginButton);

        //Get instance of FirebaseAuth object
        firebaseAuth = FirebaseAuth.getInstance();

        //Get instance of FirebaseDatabase, DatabaseReference and FirebaseStorage
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");

        login.setOnClickListener(v ->
                createNewAccount(
                        username.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString(),
                        retypedPassword.getText().toString()
                )
        );
    }

    /**
     * Operation of this method: (1) check if email is valid, if passwords match and if username string is not empty,
     * (2) begin with password-based Firebase account creation from given data, (3) while performing previous point,
     * store user image into the Firebase Storage and reference to it (its URL) to Realtime Database
     *
     * @param usernameString username string
     * @param emailString email string
     * @param passString password string
     * @param rPassString retyped password string
     */
    private void createNewAccount(String usernameString, String emailString, String passString, String rPassString) {
        ValidatorHelper validatorHelper = new ValidatorHelper();
        if (validatorHelper.isEmailValid(emailString)) {
            if (validatorHelper.doPasswordsMatch(passString, rPassString)) {
                if (!usernameString.isEmpty()) {
                    passString = validatorHelper.hashPassword(passString);
                    if (passString != null) {
                        firebaseAuth.createUserWithEmailAndPassword(emailString, passString)
                                .addOnCompleteListener(task -> {
                                    FirebaseUser firebaseUser = Objects.requireNonNull(task.getResult()).getUser();
                                    String uid = firebaseUser.getUid();
                                    databaseReference
                                            .child(uid)
                                            .setValue(new UserModel(
                                                    uid,
                                                    usernameString,
                                                    emailString,
                                                    null
                                            ));

                                    startActivity(new Intent(CreateAccountActivity.this, MainActivity.class));
                                });
                    }
                } else displayCustomToast("Can't have an empty username, can you?");
            } else displayCustomToast("Passwords don't match");
        } else displayCustomToast("Email is invalid");
    }

    /**
     * Display Toast with custom message
     *
     * @param message custom message string
     */
    private void displayCustomToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

