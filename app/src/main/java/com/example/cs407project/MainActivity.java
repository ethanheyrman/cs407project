package com.example.cs407project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        setContentView(R.layout.activity_main);

        createAccount = findViewById(R.id.create_account_button);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
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
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter a valid email.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Invalid password.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        signIn(email, password);
    }

    public void onCreateAccountClick(View view) { navigateToAccountCreationWidget(); }

    public void navigateToHome(FirebaseUser user) {
        Intent intent = new Intent(this,
                com.example.cs407project.HomeActivity.class);
        intent.putExtra("user", user);

        startActivity(intent);
    }

    public void navigateToAccountCreationWidget() {
        Intent intent = new Intent(this,
                com.example.cs407project.UserRegistrationActivity.class);
        startActivity(intent);
    }
}