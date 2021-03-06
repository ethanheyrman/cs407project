package com.example.cs407project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Arrays;

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
    GeoFire geoFire;
    DatabaseReference postsBase;
    String username;

    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
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

        username = sharedPreferences.getString("username", "");

        postsBase = FirebaseDatabase.getInstance().getReference("posts");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("geofire" + formType + "s");
        geoFire = new GeoFire(ref);
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
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    public void Submit(View view) {
        Adapter adapter = (Adapter) gridView.getAdapter();
        String[] entries = adapter.getAllEntries();
        Boolean isAllZero = true;
        for (int i = 0; i < entries.length; i++) {
            if (!entries[i].equals("0")) {
                isAllZero = false;
                break;
            }
        }
        if (isAllZero) {
            Toast.makeText(this, "No quantities have been entered", Toast.LENGTH_SHORT).show();
            return;
        }
        String json = gson.toJson(entries);
        sharedPreferences.edit().putString(formType, json).commit();
        if (currLoc == null) {
            Toast.makeText(this, "Please turn on location", Toast.LENGTH_SHORT).show();
        } else {
            if (formType.equals("request")) {
                sharedPreferences.edit().putString("Rlong", Double.toString(currLoc.getLongitude())).commit();
                sharedPreferences.edit().putString("RLat", Double.toString(currLoc.getLatitude())).commit();
            } else {
                sharedPreferences.edit().putString("Olong", Double.toString(currLoc.getLongitude())).commit();
                sharedPreferences.edit().putString("OLat", Double.toString(currLoc.getLatitude())).commit();
            }
            handleLocation();
            geoFire.setLocation(username + formType, new GeoLocation(currLoc.getLatitude(), currLoc.getLongitude()));
            postsBase.child(username + formType).child("info").setValue(Arrays.asList(entries));
            Cancel(view);
        }

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
                } else if (mLastKnownLocation == null) {
                    getLocation();
                    handleLocation();
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
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Your location is needed to send requests and offers", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(() -> {
                    handleLocation();
                }, 3500);
            }
        }
    }

    public void getLocation() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                } else {
                    mFusedLocationProviderClient.removeLocationUpdates(this);
                }
                for (Location location : locationResult.getLocations()) {
                    currLoc = location;
                }
            }
        };
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

}