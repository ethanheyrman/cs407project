package com.example.cs407project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ResourcesActivity extends AppCompatActivity {

    TextView shieldNetLink;
    TextView cdcLink;
    TextView whoLink;
    TextView nihLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        shieldNetLink = findViewById(R.id.shieldNetLink);
        shieldNetLink.setMovementMethod(LinkMovementMethod.getInstance());
        cdcLink = findViewById(R.id.cdcLink);
        cdcLink.setMovementMethod(LinkMovementMethod.getInstance());
        whoLink = findViewById(R.id.whoLink);
        whoLink.setMovementMethod(LinkMovementMethod.getInstance());
        nihLink = findViewById(R.id.nihLink);
        nihLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            item -> {
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
                        Intent addIntent = new Intent(ResourcesActivity.this, AddActivity.class);
                        startActivity(addIntent);
                        return true;
                    case R.id.action_search:
                        Intent settingsIntent = new Intent(ResourcesActivity.this, SearchActivity.class);
                        startActivity(settingsIntent);
                        return true;
                    default:
                        return false;
                }
            };
}
