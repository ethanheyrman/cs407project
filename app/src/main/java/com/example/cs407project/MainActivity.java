package com.example.cs407project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public EditText emailField;
    public EditText passwordField;
    public Button loginButton;
    public TextView createAccount;

    private FirebaseAuth mAuth;

    private FirebaseUser currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();
//        navigateToHome(currentUser);
       setContentView(R.layout.activity_main);

        createAccount = findViewById(R.id.create_account_button);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.


    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            navigateToHome(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            emailField.getText().clear();
                            passwordField.getText().clear();
                        }
                    }
                });
    }

    public void onLoginClick(View view) {
        navigateToHome(currentUser);
    }

    public void onCreateAccountClick(View view) { navigateToAccountCreationWidget(); }

    public void navigateToHome(FirebaseUser user) {
        Intent intent = new Intent(this,
                com.example.cs407project.HomeActivity.class);
        startActivity(intent);
    }

    public void navigateToAccountCreationWidget() {
        Intent intent = new Intent(this,
                com.example.cs407project.UserRegistrationActivity.class);
        startActivity(intent);
    }
}
