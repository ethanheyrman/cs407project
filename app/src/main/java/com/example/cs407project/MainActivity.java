package com.example.cs407project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("com.example.cs407project", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        gson = new Gson();

        sharedPreferences.edit().putString("username", "someguy").apply();
        String username = sharedPreferences.getString("username", "");
        DatabaseReference offerPost = FirebaseDatabase.getInstance().getReference("posts").child(username+"offer").child("info");
        DatabaseReference requestPost = FirebaseDatabase.getInstance().getReference("posts").child(username+"request").child("info");
        DatabaseReference offers = FirebaseDatabase.getInstance().getReference("geofirerequests").child(username +"request");
        DatabaseReference requests = FirebaseDatabase.getInstance().getReference("geofireoffers").child(username + "offer");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> genericTypeIndicator = new GenericTypeIndicator<List<String>>() {};
                ArrayList<String> values = (ArrayList<String>) dataSnapshot.getValue(genericTypeIndicator);
                String[] k = (String[]) values.toArray(new String[values.size()]);
                Log.i("asdf",gson.toJson(k) );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        offerPost.addListenerForSingleValueEvent(listener);
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
}