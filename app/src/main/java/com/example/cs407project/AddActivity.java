package com.example.cs407project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.gson.Gson;

public class AddActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        sharedPreferences = getSharedPreferences("com.example.cs407project", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_add);
        gson = new Gson();
    }

    public void onResume() {
        super.onResume();
        String isOffer = sharedPreferences.getString("offer", "none");
        String isRequest = sharedPreferences.getString("request", "none");

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (!isOffer.equals("none")) {
            String[] elems = gson.fromJson(isOffer, String[].class);
            fragmentTransaction.replace(R.id.frameLayout, new ListSentFragment(0, elems, sharedPreferences));
        } else {
            fragmentTransaction.replace(R.id.frameLayout, new ButtonFragment(0));
        }
        if (!isRequest.equals("none")) {
            String[] elems = gson.fromJson(isRequest, String[].class);
            fragmentTransaction.replace(R.id.frameLayout2, new ListSentFragment(1, elems, sharedPreferences));
        } else {
            fragmentTransaction.replace(R.id.frameLayout2, new ButtonFragment(1));
        }
        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_search:
                            Intent searchIntent = new Intent(AddActivity.this, SearchActivity.class);
                            startActivity(searchIntent);
                            return true;
                        case R.id.action_profile:
                            Intent profileIntent = new Intent(AddActivity.this, ProfileActivity.class);
                            startActivity(profileIntent);
                            return true;
                        case R.id.action_home:
                            Intent navigationIntent = new Intent(AddActivity.this, HomeActivity.class);
                            startActivity(navigationIntent);
                            return true;
                        case R.id.action_add:
                            Intent addIntent = new Intent(AddActivity.this, AddActivity.class);
                            startActivity(addIntent);
                            return true;
                        case R.id.action_resources:
                            Intent settingsIntent = new Intent(AddActivity.this, ResourcesActivity.class);
                            startActivity(settingsIntent);
                            return true;
                        default:
                            return false;
                    }
                }

            };
}
