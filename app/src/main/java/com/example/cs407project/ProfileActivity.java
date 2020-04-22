package com.example.cs407project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_resources:
                            Intent searchIntent = new Intent(ProfileActivity.this, ResourcesActivity.class);
                            startActivity(searchIntent);
                            return true;
                        case R.id.action_profile:
                            Intent profileIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                            startActivity(profileIntent);
                            return true;
                        case R.id.action_home:
                            Intent navigationIntent = new Intent(ProfileActivity.this, HomeActivity.class);
                            startActivity(navigationIntent);
                            return true;
                        case R.id.action_add:
                            Intent addIntent = new Intent(ProfileActivity.this, SelectionActivity.class);
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
