package com.example.cs407project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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

public class HomeActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    GeoLocation geoLocation;
    GeoFire geoFire;
    DatabaseReference offerGeolocations;
    DatabaseReference requestGeolocations;
    ArrayList<GeoLocation> list;
    ArrayList<String> strings;
    ArrayList<PostWithInfo> all;
    FirebaseAuth mAuth;
    Button requestButton;
    Button offerButton;
    final String[] PPETypes = {"Cloth mask", "Surgical Mask", "Disposable Respirator", "Half Mask",
            "Full Mask", "Mask Filters", "Goggles", "Face Shield", "Surgical Gown"};
    SupportMapFragment mapFragment;

    int offersOrRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        requestButton = findViewById(R.id.requestsButton);
        offerButton = findViewById(R.id.offersButton);
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        FirebaseApp.initializeApp(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        offerGeolocations = FirebaseDatabase.getInstance().getReference("geofireoffers");
        requestGeolocations = FirebaseDatabase.getInstance().getReference("geofirerequests");
        geoFire = new GeoFire(offerGeolocations);
        list = new ArrayList<GeoLocation>();
        strings = new ArrayList<String>();
        all = new ArrayList<PostWithInfo>();
        mAuth = FirebaseAuth.getInstance();
        offersOrRequests = 0;
        handleLocation(offersOrRequests);

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

    private void handleLocation(int isoffersOrRequest) {
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
                    all.clear();
                    strings.clear();
                    list.clear();
                    if (isoffersOrRequest == 0) {
                        geoFire = new GeoFire(requestGeolocations);
                    } else {
                        geoFire = new GeoFire(offerGeolocations);
                    }
                    getNearby();
                } else if (mLastKnownLocation == null) {
                    getLocation();
                    handleLocation(offersOrRequests);
                }
            });
        }
    }

    public void getNearby() {
        GeoQuery geoQuery = geoFire.queryAtLocation(geoLocation, 25);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (strings.size() < 20) {
                    if (!(key.equals(mAuth.getUid() + "request")) && !(key.equals(mAuth.getUid() + "offer"))) {
                        strings.add(key);
                        list.add(location);
                    }
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
                    foundPost.phone = (String) data.child("phone").getValue().toString().replace("-","");
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
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            mMap.clear();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(geoLocation.latitude, geoLocation.longitude), 11));
            for (int i = 0; i < all.size(); i++) {
                PostWithInfo curr = all.get(i);
                String postTitle = curr.name;
                if (curr.organization != null && !curr.organization.isEmpty()) {
                    postTitle = postTitle + " from " + curr.organization;
                }
                if (offersOrRequests == 0) {
                    postTitle = postTitle + "'s request";
                } else {
                    postTitle = postTitle + "'s offer";
                }

                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(curr.location.latitude, curr.location.longitude))
                        .title(postTitle))
                        .setTag(curr);
            }

            GoogleMap.OnMarkerClickListener mapClickListener = marker -> {
                PostWithInfo curr = (PostWithInfo) marker.getTag();
                String post = "";
                String[] entries = curr.PPEList;
                for (int i = 0; i < entries.length; i++) {
                    if (!entries[i].equals("0")) {
                        if (entries[i].equals("1") || PPETypes[i].endsWith("s")) {
                            post = post + entries[i] + " " + PPETypes[i] + "\n";
                        } else {
                            post = post + entries[i] + " " + PPETypes[i] + "s\n";
                        }
                    }
                }

                String phone = curr.phone;
                String phoneMessage;
                if (offersOrRequests == 0) {
                    phoneMessage = "Hello! I would like to offer PPE";
                } else {
                    phoneMessage = "Hello, I would like to request PPE";
                }

                MaterialAlertDialogBuilder selectionWindow = new MaterialAlertDialogBuilder(this)
                        .setTitle(marker.getTitle())
                        .setMessage(post)
                        .setNeutralButton("CLOSE", (dialog, which) -> dialog.dismiss())
                        .setNegativeButton("EMAIL", (dialog, which) -> {
                            Intent messagingIntent = new Intent(Intent.ACTION_SENDTO);
                            messagingIntent.setData(Uri.parse("mailto:" + Uri.encode(curr.email)));
                            startActivity(messagingIntent);
                        })
                        .setPositiveButton("PHONE", (dialog, which) -> {
                            Intent messagingIntent = new Intent(Intent.ACTION_SENDTO);
                            messagingIntent.setData(Uri.parse("smsto:" + Uri.encode(phone)));
                            messagingIntent.putExtra("sms_body", phoneMessage);
                            startActivity(messagingIntent);
                        });
                selectionWindow.show();
                return false;
            };
            mMap.setOnMarkerClickListener(mapClickListener);
        });
    }

    public void showRequests(View view) {
        requestButton.setBackgroundColor(Color.parseColor("#ff99cc00"));
        offerButton.setBackgroundColor(Color.parseColor("#ffaaaaaa"));
        handleLocation(0);
        offersOrRequests = 0;
    }

    public void showOffers(View view) {
        offerButton.setBackgroundColor(Color.parseColor("#ff99cc00"));
        requestButton.setBackgroundColor(Color.parseColor("#ffaaaaaa"));
        handleLocation(1);
        offersOrRequests = 1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handleLocation(offersOrRequests);
            }
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Your location is needed to view nearby requests and offers", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(() -> {
                    handleLocation(offersOrRequests);
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
                    default:
                        return false;
                }
            };
}