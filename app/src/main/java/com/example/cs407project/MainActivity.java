package com.example.cs407project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private String usernameInput;
    private String passwordInput;
    private TextInputEditText mUsernameField;
    private TextInputEditText mPasswordField;
    private Button mLoginButton;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsernameField = findViewById(R.id.login_username_text);
        mPasswordField = findViewById(R.id.login_password_text);
        mLoginButton = findViewById(R.id.login_button);
        mRegisterButton = findViewById(R.id.register_button);

        mLoginButton.setOnClickListener(
                v -> {
                    usernameInput = mUsernameField.getText().toString();
                    passwordInput = mPasswordField.getText().toString();
                    Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);
                    loginIntent.putExtra("username", usernameInput);
                    loginIntent.putExtra("password", passwordInput);
                    startActivity(loginIntent);
                }
        );
        mRegisterButton.setOnClickListener(
                v -> {
                    Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                }
        );
    }



}
