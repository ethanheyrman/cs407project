package com.example.cs407project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cs407project.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";

    private static final String EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";

    public EditText emailField;
    public EditText passwordField;
    public EditText confirmPasswordField;
    public Button registerButton;
    public CheckBox isOrganizationButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Change activity size
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .6));
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        setContentView(R.layout.activity_user_registration);

        emailField = (EditText) findViewById(R.id.register_email);
        passwordField = (EditText) findViewById(R.id.register_password);
        confirmPasswordField = (EditText) findViewById(R.id.confirm_password);
        registerButton = (Button) findViewById(R.id.register_button);
//        isOrganizationButton = (CheckBox) findViewById(R.id.is_organization_button);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void onCreateAccountClick(View view) {
        String email = emailField.getText().toString();
        Log.i("message", email);

        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();
//        boolean isOrganization = isOrganizationButton.isChecked();

        if (!TextUtils.isEmpty(passwordField.getText().toString()) &&
                !TextUtils.isEmpty(confirmPasswordField.getText().toString()))
            if (validateAndConfirmPassword(password, confirmPassword)) {
                createAccount(email, password); //isOrganization);
            } else {
                Toast.makeText(UserRegistrationActivity.this, "Passwords do not match.",
                        Toast.LENGTH_SHORT).show();
                passwordField.getText().clear();
                confirmPasswordField.getText().clear();
        }
        else if (TextUtils.isEmpty(passwordField.getText().toString())) {
            if (TextUtils.isEmpty(confirmPasswordField.getText().toString()))
                Toast.makeText(UserRegistrationActivity.this, "Please confirm your password.",
                        Toast.LENGTH_SHORT).show();
            else Toast.makeText(UserRegistrationActivity.this, "Password is required.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateAndConfirmPassword(String password, String confirmPassword) {

        if ((confirmPassword != null && password != null)) {

            Pattern pattern;
            Matcher matcher;
            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(password);

            return (matcher.matches() && password.equals(confirmPassword));
        }
        return false;
    }

    private void createAccount(final String email, String password) { //}, final boolean isOrganization) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

//                            if (isOrganization){}
//
//                            else {
//                                User newUser = new User(email);
//
//                                mDatabase.child("users").child(userId).setValue(user);
//                            }

                            navigateToHome(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(UserRegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            emailField.getText().clear();
                            passwordField.getText().clear();
                            confirmPasswordField.getText().clear();
                        }
                    }
                });
    }

    public void navigateToHome(FirebaseUser user) {
        Intent intent = new Intent(this,
                com.example.cs407project.HomeActivity.class);
        startActivity(intent);
    }
}