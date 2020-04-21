package com.example.cs407project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private String registerEmail;
    private String registerUsername;
    private String registerPassword;
    private TextInputEditText mUsernameField;
    private TextInputEditText mPasswordField;
    private TextInputEditText mEmailField;
    private Button mConfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsernameField = findViewById(R.id.register_username_text);
        mPasswordField = findViewById(R.id.register_password_text);
        mEmailField = findViewById(R.id.register_email_text);
        mConfirmButton = findViewById(R.id.register_confirm);

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerEmail = mEmailField.getText().toString();
                registerUsername = mUsernameField.getText().toString();
                registerPassword = mPasswordField.getText().toString();
                Intent registerIntent = new Intent(RegisterActivity.this, MainActivity.class);
                registerIntent.putExtra("register_email", registerEmail);
                registerIntent.putExtra("register_username", registerUsername);
                registerIntent.putExtra("register_password", registerPassword);
                startActivity(registerIntent);
            }
        });

    }
}
