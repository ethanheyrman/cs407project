package com.example.cs407project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient; //save the instance
    private final LatLng mDestinationLatLng = new LatLng(43.0715255, -89.4088546);
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12; //could've been any number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            //code to display marker
            googleMap.addMarker(new MarkerOptions()
                    .position(mDestinationLatLng)
                    .title("Destination"));
            displayMyLocation();
        });
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.action_resources:
                        Intent resourcesIntent = new Intent(HomeActivity.this, ResourcesActivity.class);
                        startActivity(resourcesIntent);
                        return true;
                    case R.id.action_profile:
                        Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(profileIntent);
                        return true;
                    case R.id.action_home:
                        Intent navigationIntent = new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(navigationIntent);
                        return true;
                    case R.id.action_add:
                        Intent addIntent = new Intent(HomeActivity.this, SelectionActivity.class);
                        startActivity(addIntent);
                        return true;
                    case R.id.action_search:
                        Intent settingsIntent = new Intent(HomeActivity.this, SearchActivity.class);
                        startActivity(settingsIntent);
                        return true;
                    default:
                        return false;
                }
            };

    private void displayMyLocation() {
        //Check if permission granted
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        //If not, ask for it
        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        //If permission granted, display marker at current location
        else {
            mFusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(this, task -> {
                        Location mLastKnownLocation = task.getResult();
                        if (task.isSuccessful() && mLastKnownLocation != null) {
                            System.out.println("This is the last known location: " + mLastKnownLocation);
                            mMap.addPolyline(new PolylineOptions()
                                    .add(new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), mDestinationLatLng));
                        }
                    });
        }
    }

}
