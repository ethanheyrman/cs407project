package com.example.cs407project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker mTest;
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

        mapFragment.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mTest = mMap.addMarker(new MarkerOptions()
                .position(mDestinationLatLng)
                .title("test destination"));
        displayMyLocation();
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        MaterialAlertDialogBuilder selectionWindow = new MaterialAlertDialogBuilder(this)
                .setTitle("Item Selected")
                .setMessage("Test Info")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("MESSAGE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent messagingIntent = new Intent(Intent.ACTION_SENDTO);
                        messagingIntent.setData(Uri.parse("smsto:" + Uri.encode("18002221111")));
                        messagingIntent.putExtra("sms_body", "Hello! I would like to provide/request PPE");
                        startActivity(messagingIntent);
                    }
                });
        selectionWindow.show();

        return false;
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
                        Intent addIntent = new Intent(HomeActivity.this, AddActivity.class);
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
}
