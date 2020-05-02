package com.example.cs407project;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    GeoLocation geoLocation;
    GeoFire geoFire;
    DatabaseReference geolocations;
    ArrayList<GeoLocation> list;
    ArrayList<String> strings;
    ArrayList<PostWithInfo> all;
    FirebaseAuth mAuth;
    final String[] PPETypes = {"Cloth mask", "Surgical Mask", "Disposable Respirator", "Half Mask",
            "Full Mask", "Mask Filters", "Goggles", "Face Shield", "Surgical Gown"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geolocations = FirebaseDatabase.getInstance().getReference("geofireoffers");
        geoFire = new GeoFire(geolocations);
        list = new ArrayList<GeoLocation>();
        strings = new ArrayList<String>();
        all = new ArrayList<PostWithInfo>();
        mAuth = FirebaseAuth.getInstance();
        handleLocation();
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
                    geoLocation = new GeoLocation(location.getLatitude(), location.getLongitude());
                }
            }
        };
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
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

                    geoLocation = new GeoLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                    getNearby();
                } else if (mLastKnownLocation == null) {
                    getLocation();
                    handleLocation();
                }
            });
        }
    }

    public void getNearby() {
        GeoQuery geoQuery = geoFire.queryAtLocation(geoLocation, 10);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (strings.size() < 20) {
                    if (!key.equals(mAuth.getUid()))
                        strings.add(key);
                    list.add(location);
                }
            }

            @Override
            public void onKeyExited(String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onGeoQueryReady() {
                for (int i = 0; i < strings.size(); i++) {
                    fillInfo(strings.get(i), i);
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("There was an error with this query: " + error);
            }
        });
    }


    public void fillInfo(String key, int place) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("posts").child(key).child("info");
        ValueEventListener listener = new eventListener(place, key);
        ref.addListenerForSingleValueEvent(listener);
    }

    public void fillUser(String uuid, String[] post, int place) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        Query userQuery = ref.orderByChild("uuid").equalTo(uuid);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    PostWithInfo foundPost = new PostWithInfo();
                    foundPost.PPEList = post;
                    foundPost.name = (String) data.child("firstName").getValue();
                    foundPost.email = (String) data.child("email").getValue();
                    foundPost.organization = (String) data.child("organizationName").getValue();
                    foundPost.location = list.get(place);
                    all.add(foundPost);
                    if (all.size() == strings.size()) {
                        display();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void display() {

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

    class eventListener implements ValueEventListener {
        int number;
        String key;

        eventListener(int number, String key) {
            this.number = number;
            this.key = key;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<List<String>> genericTypeIndicator = new GenericTypeIndicator<List<String>>() {
            };
            ArrayList<String> values = (ArrayList<String>) dataSnapshot.getValue(genericTypeIndicator);
            if (values != null && values.size() != 0) {
                String[] offer = values.toArray(new String[values.size()]);
                fillUser(key.substring(0, 28), offer, number);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    }
}
