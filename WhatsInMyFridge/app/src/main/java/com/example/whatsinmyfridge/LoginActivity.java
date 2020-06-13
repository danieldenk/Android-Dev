package com.example.whatsinmyfridge;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
    // The firebase object that is being referenced for login functionality
    FirebaseAuth firebaseAuth;

    // Suppression needed for the Color change of the button
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Changing the opacity of the background image
        findViewById(R.id.loginLayout).getBackground().setAlpha(140);

        // Initializing Firebase
        FirebaseApp.initializeApp(this);
        firebaseAuth = getInstance();

        // Initializing all of the necessary objects
        Button login = (Button) findViewById(R.id.login_login);
        Button signup = (Button) findViewById(R.id.login_signup);
        Button loginGuest = (Button) findViewById(R.id.login_guest);
        TextView forgottenPw = (TextView) findViewById(R.id.forgottenPw);
        final EditText mail = (EditText) findViewById(R.id.login_mail);
        final EditText pw = (EditText) findViewById(R.id.login_pw);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.loginProgressBar);

        // Adding View animation (Bouncing title)
        YoYo.with(Techniques.BounceInDown).duration(1000).repeat(0).repeatMode(1).playOn(findViewById(R.id.titleViewLogin));

        // Setting layouts for cosmetic purposes
        login.getBackground().setColorFilter(Color.parseColor(getResources().getString(R.color.freshGreenish)), PorterDuff.Mode.SRC_ATOP);
        loginGuest.getBackground().setColorFilter(Color.parseColor(getResources().getString(R.color.freshGreenish)), PorterDuff.Mode.SRC_ATOP);
        mail.getBackground().setColorFilter(Color.parseColor("white"), PorterDuff.Mode.SRC_ATOP);
        pw.getBackground().setColorFilter(Color.parseColor("white"), PorterDuff.Mode.SRC_ATOP);
        forgottenPw.setMovementMethod(LinkMovementMethod.getInstance());
        forgottenPw.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        // Creating an onclick listener for the signup part
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Setting the progressbar to visible so the user sees that sth is happening
                progressBar.setVisibility(ProgressBar.VISIBLE);
                // Getting mail and pw input text
                String mail_txt = mail.getText().toString();
                String pw_txt = pw.getText().toString();
                // If either the mail or pw field is empty we tell the user to fill out everything
                if (mail_txt.equals("") || pw_txt.equals("")) {
                    Snackbar.make(v, "Please fill out all of the required fields!", Snackbar.LENGTH_SHORT).show();
                    // Animation for faulty input
                    shakeFields();
                    // Reaching state "finished" -> Hiding progressbar
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                } else {
                    // else we try to create a user
                    firebaseAuth.createUserWithEmailAndPassword(mail_txt.trim(), pw_txt.trim()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // if that does not work e.g. the mail is not a mail, the user gets an error
                            if (!task.isSuccessful()) {
                                Snackbar.make(v, "Sign-Up could not be completed. Please check your entered data and try again!", Snackbar.LENGTH_SHORT).show();
                                // Animation for faulty input
                                shakeFields();
                                // Reaching state "finished" -> Hiding progressbar
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                            } else {
                                // Else the sign-up worked and the user can use the app
                                startActivity(new Intent(LoginActivity.this, LoggedInActivity.class));
                                Toast.makeText(getBaseContext(), "Your account has been created.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        // Creating the functionality concerning the password reset
        forgottenPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting mail
                String mailText = mail.getText().toString();
                // Setting the progressbar to visible so the user sees that sth is happening
                progressBar.setVisibility(ProgressBar.VISIBLE);
                if (mailText.equals("")) {
                    // Animation for faulty input
                    YoYo.with(Techniques.Shake).repeat(1).playOn(findViewById(R.id.login_mail));
                    YoYo.with(Techniques.Shake).repeat(1).playOn(findViewById(R.id.loginTxtMail));
                    Toast.makeText(getApplicationContext(), "Please insert a valid mail address!", Toast.LENGTH_LONG).show();
                    // Reaching state "finished" -> Hiding progressbar
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                } else {
                    firebaseAuth.sendPasswordResetEmail(mailText).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // If that does not work e.g. wrong password or mail the user gets another error
                            if (!task.isSuccessful()) {
                                // The next lines are here to create a toast as a form of feedback where the text is centered
                                Toast msg = Toast.makeText(getApplicationContext(), "Sorry, there has been an error sending the password reset request!\nPlease check if the mail address is correct!", Toast.LENGTH_LONG);
                                TextView msg_txt = (TextView) msg.getView().findViewById(android.R.id.message);
                                msg_txt.setGravity(Gravity.CENTER);
                                msg.show();
                                // Animation for faulty input
                                shakeFields();
                                // Reaching state "finished" -> Hiding progressbar
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                            } else {
                                Toast.makeText(getApplicationContext(), "A password reset request has been sent to your mail!", Toast.LENGTH_LONG).show();
                                // Reaching state "finished" -> Hiding progressbar
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });

        // Creating the functionality concerning the login as a guest functionality
        // THE DIFFERENCE IS THAT A NORMAL USER WILL LATER BE ABLE TO ADD HIS OWN RECIPES (THE GUEST CAN'T)!
        loginGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Setting the progressbar to visible so the user sees that sth is happening
                progressBar.setVisibility(ProgressBar.VISIBLE);
                firebaseAuth.signInAnonymously().addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Sorry, you could not be logged in.\nPlease try again!", Toast.LENGTH_LONG).show();
                            // Reaching state "finished" -> Hiding progressbar
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                        } else {
                            Toast.makeText(getApplicationContext(), "\t\tYou were succesfully logged in.\nPlease consider creating an account!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, LoggedInActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        // Creating an onclick listener for the login itself
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Setting the progressbar to visible so the user sees that sth is happening
                progressBar.setVisibility(ProgressBar.VISIBLE);
                // Getting mail and pw input text
                String mail_txt = mail.getText().toString();
                String pw_txt = pw.getText().toString();
                // If either of the inputs is empty the user gets an error
                if (mail_txt.equals("") || pw_txt.equals("")) {
                    Snackbar.make(v, "Please enter all of the fields!", Snackbar.LENGTH_SHORT).show();
                    // Animation for faulty input
                    shakeFields();
                    // Reaching state "finished" -> Hiding progressbar
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                } else {
                    // Otherwise we try the login
                    firebaseAuth.signInWithEmailAndPassword(mail_txt.trim(), pw_txt.trim()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If that does not work e.g. wrong password or mail the user gets another error
                            if (!task.isSuccessful()) {
                                Snackbar.make(v, "Sorry you have entered the wrong credentials or this user is unknown!", Snackbar.LENGTH_SHORT).show();
                                // Animation for faulty input
                                shakeFields();
                                // Reaching state "finished" -> Hiding progressbar
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                            } else {
                                // Otherwise if everything works as intended the user is logged in and is allowed to enter the app
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

    // Simple method which is there to animate the input fields
    // I have sourced it out for easier maintenance and editing
    public void shakeFields() {
        YoYo.with(Techniques.Shake).repeat(1).playOn(findViewById(R.id.login_mail));
        YoYo.with(Techniques.Shake).repeat(1).playOn(findViewById(R.id.login_pw));
        YoYo.with(Techniques.Shake).repeat(1).playOn(findViewById(R.id.loginTxtMail));
        YoYo.with(Techniques.Shake).repeat(1).playOn(findViewById(R.id.loginTxtPw));
    }

    @Override
    public void onPause() {
        super.onPause();
        // Reaching state "finished" -> Hiding progressbar
        // For all of the cases where the activity is changed
        // (Needed because it shows a more accurate loading status in comparison to stop when button clicked)
        ProgressBar p = findViewById(R.id.loginProgressBar);
        p.setVisibility(ProgressBar.INVISIBLE);
    }
}