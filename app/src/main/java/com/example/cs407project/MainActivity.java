package com.example.cs407project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.cs407project", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);

        //maybe usefragments for the 2 sections
        //each button can be in its own view, and these views are programmatically filled with segments.
    }

    public void request(View view)
    {
        Intent intent = new Intent(this,SendPage.class);
        intent.putExtra("type", "I want:");
        startActivity(intent);
    }

    public void offer(View view)
    {
        Intent intent = new Intent(this,SendPage.class);
        intent.putExtra("type", "I have:");
        startActivity(intent);
    }
}
