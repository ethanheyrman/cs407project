package com.example.cs407project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ScrollView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    public TextInputEditText firstName;
    public TextInputEditText lastName;
    public TextInputEditText occupation;
    public EditText age;
    public ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Log.i("TAG", String.valueOf(currentUser.getEmail()));

        firstName = findViewById(R.id.first_name_container).findViewById(R.id.first_name);
        firstName.setText(currentUser.getEmail());
//        firstName.post(new Runnable() {
//            @Override
//            public void run() {
//                firstName.setText(currentUser.getEmail());
//            }
//        });


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_search:
                            Intent searchIntent = new Intent(ProfileActivity.this, SearchActivity.class);
                            startActivity(searchIntent);
                            return true;
                        case R.id.action_profile:
                            Intent profileIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                            startActivity(profileIntent);
                            return true;
                        case R.id.action_navigation:
                            Intent navigationIntent = new Intent(ProfileActivity.this, NavigationActivity.class);
                            startActivity(navigationIntent);
                            return true;
                        case R.id.action_add:
                            Intent addIntent = new Intent(ProfileActivity.this, AddActivity.class);
                            startActivity(addIntent);
                            return true;
                        case R.id.action_settings:
                            Intent settingsIntent = new Intent(ProfileActivity.this, SettingsActivity.class);
                            startActivity(settingsIntent);
                            return true;
                        default:
                            return false;
                    }
                }

            };
}
