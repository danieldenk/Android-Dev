package com.example.whatsinmyfridge;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

/**
 * This is the class handling the Login Functionality with Firebase
 */
public class LoginActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    // Suppression Needed for the Color change of the button
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initializing Firebase
        FirebaseApp.initializeApp(this);
        firebaseAuth = getInstance();

        // Initializing all of the necessary objects
        Button login = (Button) findViewById(R.id.login_login);
        Button signup = (Button) findViewById(R.id.login_signup);
        EditText mail = (EditText) findViewById(R.id.login_mail);
        EditText pw = (EditText) findViewById(R.id.login_pw);

        // Setting layouts for cosmetic purposes
        login.getBackground().setColorFilter(Color.parseColor(getResources().getString(R.color.freshGreenish)), PorterDuff.Mode.SRC_ATOP);
        mail.getBackground().setColorFilter(Color.parseColor("white"), PorterDuff.Mode.SRC_ATOP);
        pw.getBackground().setColorFilter(Color.parseColor("white"), PorterDuff.Mode.SRC_ATOP);

        // Creating an onclick listener for the signup part
        signup.setOnClickListener(new View.OnClickListener() {
            EditText mail = (EditText) findViewById(R.id.login_mail);
            EditText pw = (EditText) findViewById(R.id.login_pw);

            @Override
            public void onClick(final View v) {
                String mail_txt = mail.getText().toString();
                String pw_txt = pw.getText().toString();
                // If either the mail or pw field is empty we tell the user to fill out everything
                if (mail_txt.equals("") || pw.equals(""))
                    Snackbar.make(v, "Please fill out all of the required fields!", Snackbar.LENGTH_SHORT).show();
                else {
                    // else we try to create a user
                    firebaseAuth.createUserWithEmailAndPassword(mail_txt.trim(), pw_txt.trim()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // if that does not work e.g. the mail is not a mail, the user gets an error
                            if (!task.isSuccessful()) {
                                Snackbar.make(v, "Sign-Up could not be completed. Please check your entered data and try again!", Snackbar.LENGTH_SHORT).show();
                            } else {
                                // Else the signup worked and the user can use the app
                                startActivity(new Intent(LoginActivity.this, LoggedInActivity.class));
                                Toast.makeText(getBaseContext(), "Your account has been created.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        // Creating an onclick listener for the login itself
        login.setOnClickListener(new View.OnClickListener() {
            EditText mail = (EditText) findViewById(R.id.login_mail);
            EditText pw = (EditText) findViewById(R.id.login_pw);

            @Override
            public void onClick(final View v) {
                String mail_txt = mail.getText().toString();
                String pw_txt = pw.getText().toString();
                // If either of the inputs is empty the user gets an error
                if (mail_txt.equals("") || pw_txt.equals(""))
                    Snackbar.make(v, "Please enter all of the fields!", Snackbar.LENGTH_SHORT).show();
                else {
                    // Otherwise we try the login
                    firebaseAuth.signInWithEmailAndPassword(mail_txt.trim(), pw_txt.trim()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If that does not work e.g. wrong password or mail the user gets another error
                            if (!task.isSuccessful()) {
                                Snackbar.make(v, "Sorry you have entered the wrong credentials or this user is unknown!", Snackbar.LENGTH_SHORT).show();
                            } else {
                                // Otherwise if everything works as intered the user is logged in and is allowed to enter the app
                                Toast.makeText(getBaseContext(), "You have been logged in.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, LoggedInActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }
}