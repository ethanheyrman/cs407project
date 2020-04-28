package com.example.cs407project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

public class SendPage extends AppCompatActivity {

    GridView gridView;
    SharedPreferences sharedPreferences;
    String formType;
    final String[] PPETypes = {"Cloth mask", "Surgical Mask", "Disposable Respirator", "Half Mask",
            "Full Mask", "Mask Filters", "Goggles", "Face Shield", "Surgical Gown"};
    Gson gson;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    Location currLoc;

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
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        handleLocation();
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
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void Submit(View view) {
        Adapter adapter = (Adapter) gridView.getAdapter();
        String[] entries = adapter.getAllEntries();
        String json = gson.toJson(entries);
        sharedPreferences.edit().putString(formType, json).commit();
        if(formType.equals("request"))
        {
            sharedPreferences.edit().putString("Rlong",Double.toString(currLoc.getLongitude())).apply();
            sharedPreferences.edit().putString("RLat",Double.toString(currLoc.getLatitude())).apply();
        }
        else
        {
            sharedPreferences.edit().putString("Olong",Double.toString(currLoc.getLongitude())).apply();
            sharedPreferences.edit().putString("OLat",Double.toString(currLoc.getLatitude())).apply();
        }
        handleLocation();
        Cancel(view);
    }

    private void handleLocation() {
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, task -> {
                Location mLastKnownLocation = task.getResult();
                if (task.isSuccessful() && mLastKnownLocation != null) {
                    currLoc = mLastKnownLocation;
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handleLocation();
            }
        }
    }
}