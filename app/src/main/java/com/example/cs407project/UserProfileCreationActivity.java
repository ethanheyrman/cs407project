package com.example.cs407project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cs407project.models.User;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileCreationActivity extends AppCompatActivity {
    private static final String TAG = "ProfileCreationActivity";

    public EditText firstName;
    public EditText lastName;
    public EditText phone;
    public EditText email;
    public SwitchMaterial isOrganization;
    public EditText organizationName;
    public View organizationNameContainer;
    public Button createProfileButton;
    public ImageView profileImage;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_creation);
        firstName = findViewById(R.id.first_name_container).findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name_container).findViewById(R.id.last_name);
        phone = findViewById(R.id.phone_container).findViewById(R.id.phone);
        email = findViewById(R.id.email_container).findViewById(R.id.email);
        isOrganization = findViewById(R.id.org_switch);
        organizationName = findViewById(R.id.organization_name_container).findViewById(R.id.organization_name);
        organizationNameContainer = findViewById(R.id.organization_name_container);
        createProfileButton = findViewById(R.id.complete_profile_button);
        profileImage = findViewById(R.id.profile_image);

        isOrganization.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("TAG", String.valueOf(isChecked));
                organizationName.getText().clear();
                if (isChecked)
                   organizationNameContainer.setVisibility(View.VISIBLE);
                else organizationNameContainer.setVisibility(View.INVISIBLE);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance().getReference();

        email.setText(currentUser.getEmail());
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
        String newUserPhone = phone.getText().toString();
        String newUserFirstName = firstName.getText().toString();
        String newUserLastName = lastName.getText().toString();
        boolean newUserOrgStatus = isOrganization.isChecked();
        String newUserOrgName = null;
        if ((isOrganization.isChecked())) {
            newUserOrgName = organizationName.getText().toString();
        }

        User user = new User(
            newUserUUID, newUserEmail, newUserPhone, newUserFirstName, newUserLastName,
                newUserOrgStatus, newUserOrgName
        );
        database.child("users").child(currentUser.getUid()).setValue(user);

    }
}

