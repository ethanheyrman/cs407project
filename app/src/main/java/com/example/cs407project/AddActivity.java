package com.example.cs407project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Gson gson;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        sharedPreferences = getSharedPreferences("com.example.cs407project", Context.MODE_PRIVATE);
        sharedPreferences.edit().remove("offer").commit();
        sharedPreferences.edit().remove("request").commit();

        gson = new Gson();
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences.edit().putString("username", mAuth.getUid()).commit();
        DatabaseReference offerPost = FirebaseDatabase.getInstance().getReference("posts").child(mAuth.getUid() + "offer").child("info");
        DatabaseReference requestPost = FirebaseDatabase.getInstance().getReference("posts").child(mAuth.getUid() + "request").child("info");

        ValueEventListener listener = new eventListener(0);
        ValueEventListener listener2 = new eventListener(1);

        offerPost.addListenerForSingleValueEvent(listener);
        requestPost.addListenerForSingleValueEvent(listener2);
    }


    public void onResume() {
        super.onResume();
        loadUI();
    }

    public void loadUI() {
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

    class eventListener implements ValueEventListener {
        int type;

        eventListener(int type) {
            this.type = type;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<List<String>> genericTypeIndicator = new GenericTypeIndicator<List<String>>() {
            };
            ArrayList<String> values = (ArrayList<String>) dataSnapshot.getValue(genericTypeIndicator);
            if (values != null && values.size() != 0) {
                String[] offer = values.toArray(new String[values.size()]);
                String json = gson.toJson(offer);
                if (type == 0) {
                    sharedPreferences.edit().putString("offer", json).commit();
                } else {
                    sharedPreferences.edit().putString("request", json).commit();
                }
                loadUI();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            item -> {
                switch (item.getItemId()) {
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
            };
}
