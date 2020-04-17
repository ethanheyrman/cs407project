package com.example.cs407project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("com.example.cs407project", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
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
}