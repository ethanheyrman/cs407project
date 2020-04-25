package com.example.cs407project;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs407project.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileCreationActivity extends AppCompatActivity {
    private static final String TAG = "ProfileCreationActivity";

    public EditText firstName;
    public EditText lastName;
    public EditText age;
    public EditText occupation;
    public Switch isOrganization;
    public EditText organizationName;
    public Button createProfileButton;
    public ImageView profileImage;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_creation);
        //TODO set up containers in xml
        firstName = findViewById(R.id.first_name_container).findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name_container).findViewById(R.id.last_name);
        age = findViewById(R.id.age_container).findViewById(R.id.age);
        occupation = findViewById(R.id.occupation_container).findViewById(R.id.occupation);
        isOrganization = findViewById(R.id.org_switch);
        organizationName = findViewById(R.id.organization_name_container).findViewById(R.id.organization_name);
        createProfileButton = findViewById(R.id.complete_profile_button);
        profileImage = findViewById(R.id.profile_image);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance().getReference();
    }



    public void onCreateAccountClick(View view) {
        createProfile();
        Intent intent = new Intent(this,
                com.example.cs407project.HomeActivity.class);
        startActivity(intent);
    }
    private void createProfile() {
        String newUserUUID = currentUser.getUid();
        String newUserEmail = currentUser.getEmail();
        String newUserAge = age.getText().toString();
        String newUserFirstName = firstName.getText().toString();
        String newUserLastName = lastName.getText().toString();
        String newUserOccupation = occupation.getText().toString();
        boolean newUserOrgStatus = false;
        String newUserOrgName = null;
        if (Boolean.parseBoolean(isOrganization.getText().toString())) {
            newUserOrgStatus = isOrganization.isChecked();
            newUserOrgName = isOrganization.getText().toString();
        }

        User user = new User(
            newUserUUID, newUserEmail, newUserFirstName, newUserLastName,
                newUserOccupation, newUserAge, newUserOrgStatus, newUserOrgName
        );

        database.child("users").child(currentUser.getUid()).setValue(user);

    }
}

