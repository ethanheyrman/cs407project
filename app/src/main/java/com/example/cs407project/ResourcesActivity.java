package com.example.cs407project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ResourcesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_resources:
                            Intent searchIntent = new Intent(ResourcesActivity.this, ResourcesActivity.class);
                            startActivity(searchIntent);
                            return true;
                        case R.id.action_profile:
                            Intent profileIntent = new Intent(ResourcesActivity.this, ProfileActivity.class);
                            startActivity(profileIntent);
                            return true;
                        case R.id.action_home:
                            Intent navigationIntent = new Intent(ResourcesActivity.this, HomeActivity.class);
                            startActivity(navigationIntent);
                            return true;
                        case R.id.action_add:
                            Intent addIntent = new Intent(ResourcesActivity.this, SelectionActivity.class);
                            startActivity(addIntent);
                            return true;
                        case R.id.action_settings:
                            Intent settingsIntent = new Intent(ResourcesActivity.this, SettingsActivity.class);
                            startActivity(settingsIntent);
                            return true;
                        default:
                            return false;
                    }
                }

            };
}
