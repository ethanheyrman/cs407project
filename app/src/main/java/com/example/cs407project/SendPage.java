package com.example.cs407project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SendPage extends AppCompatActivity {

    GridView gridView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_page);

        TextView type = findViewById(R.id.type);
        type.setText(getIntent().getStringExtra("type"));

        String[] PPETypes = {"Cloth mask", "Surgical Mask", "Disposable Respirator", "Half Mask",
                "Full Mask", "Mask Filters", "Goggles", "Face Shield", "Surgical Gown"};

        Adapter adapter = new Adapter(this, PPETypes);
        gridView = findViewById(R.id.itemList);
        gridView.setAdapter(adapter);
    }

    public void Cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void Submit(View view) {

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.cs407project", Context.MODE_PRIVATE);
        //Log.i("asdfasdf", gridView.getAdapter().getItem(0).toString());

    }

}
