package com.example.cs407project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class SendPage extends AppCompatActivity {

    GridView gridView;
    SharedPreferences sharedPreferences;
    String formType;
    final String[] PPETypes = {"Cloth mask", "Surgical Mask", "Disposable Respirator", "Half Mask",
            "Full Mask", "Mask Filters", "Goggles", "Face Shield", "Surgical Gown"};
    Gson gson;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_page);

        gson = new Gson();
        sharedPreferences = getSharedPreferences("com.example.cs407project", Context.MODE_PRIVATE);

        TextView type = findViewById(R.id.type);
        formType = getIntent().getStringExtra("type");
        if (formType.equals("request")) {
            type.setText("I need: ");
        } else {
            type.setText("I have: ");
        }
    }

    public void onResume() {
        super.onResume();

        String thing = sharedPreferences.getString(formType, "none");
        Adapter adapter;
        if (!thing.equals("none")) {
            Gson gson = new Gson();
            String[] previousEntries = gson.fromJson(thing, String[].class);
            adapter = new Adapter(this, PPETypes, previousEntries);
        } else {
            adapter = new Adapter(this, PPETypes, null);
        }

        gridView = findViewById(R.id.itemList);
        gridView.setAdapter(adapter);
    }

    public void Cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void Submit(View view) {
        Adapter adapter = (Adapter) gridView.getAdapter();
        String[] entries = adapter.getAllEntries();
        String json = gson.toJson(entries);
        sharedPreferences.edit().putString(formType, json).commit();
        Cancel(view);
    }
}