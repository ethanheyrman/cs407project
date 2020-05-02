package com.example.cs407project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.cs407project.models.PPEPost;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseReference geoFireRequestsReference;
    DatabaseReference geoFireOffersReference;
    DatabaseReference postReference;
    DatabaseReference userReference;
    GeoFire geoFireRequests;
    GeoFire geoFireOffers;
    ArrayList<PPEPost> postsList = new ArrayList<>();
    ArrayList<MarkerOptions> markerList = new ArrayList<>();
    private FusedLocationProviderClient mFusedLocationProviderClient; //save the instance
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12; //could've been any number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        GoogleMap.OnMarkerClickListener mapClickListener = marker -> {
            MaterialAlertDialogBuilder selectionWindow = new MaterialAlertDialogBuilder(this)
                    .setTitle(marker.getTitle())
                    .setMessage("Test Info")
                    .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("MESSAGE", (dialog, which) -> {
                        Intent messagingIntent = new Intent(Intent.ACTION_SENDTO);
                        messagingIntent.setData(Uri.parse("smsto:" + Uri.encode("18002221111")));
                        messagingIntent.putExtra("sms_body",
                                "Hello! I would like to provide/request PPE");
                        startActivity(messagingIntent);
                    });
            selectionWindow.show();
            return false;
        };

        geoFireRequestsReference = FirebaseDatabase.getInstance().getReference("geofirerequests");
        geoFireRequests = new GeoFire(geoFireRequestsReference);
        GeoQuery geoQueryRequests = geoFireRequests
                .queryAtLocation(new GeoLocation(42.9915549, -88.0827346), 20);

        geoFireOffersReference = FirebaseDatabase.getInstance().getReference("geofireoffers");
        geoFireOffers = new GeoFire(geoFireOffersReference);
        GeoQuery geoQueryOffers = geoFireOffers
                .queryAtLocation(new GeoLocation(42.9915549, -88.0827346), 20);

        postReference = FirebaseDatabase.getInstance().getReference().child("posts");
        userReference = FirebaseDatabase.getInstance().getReference().child("users");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        geoQueryRequests.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                PPEPost newPPE = new PPEPost();
                newPPE.type = "request";
                newPPE.id = key.replace("request", "");
                newPPE.location = location;
                Query postQuery = postReference.orderByChild("authorUUID").equalTo(key);
                postQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            GenericTypeIndicator<List<String>> genericTypeIndicator =
                                    new GenericTypeIndicator<List<String>>() {
                                    };
                            ArrayList<String> values = (ArrayList<String>) data
                                    .child("info")
                                    .getValue(genericTypeIndicator);
                            newPPE.PPEList = values.toArray(new String[values.size()]);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Query userQuery = userReference.orderByChild("uuid").equalTo(newPPE.id);
                userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            newPPE.name = (String) data.child("firstName").getValue();
                            newPPE.email = (String) data.child("email").getValue();
                            newPPE.phone = (String) data.child("phone").getValue();
                            boolean isOrg = (Boolean) data.child("isOrganization").getValue();
                            if (isOrg) {
                                newPPE.organization = (String) data.child("organizationName").getValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                postsList.add(newPPE);
                MarkerOptions newMarker = new MarkerOptions()
                        .position(new LatLng(location.latitude, location.longitude))
                        .title(newPPE.id + " " + newPPE.type);
                mMap.addMarker(newMarker);
                mMap.setOnMarkerClickListener(mapClickListener);
                markerList.add(newMarker);
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
                System.out.println("All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("There was an error with this query: " + error);
            }
        });

        geoQueryOffers.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                PPEPost newPPE = new PPEPost();
                newPPE.type = "offer";
                newPPE.id = key.replace("offer", "");
                newPPE.location = location;
                Query postQuery = postReference.orderByChild("authorUUID").equalTo(key);
                postQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            GenericTypeIndicator<List<String>> genericTypeIndicator =
                                    new GenericTypeIndicator<List<String>>() {
                                    };
                            ArrayList<String> values = (ArrayList<String>) data
                                    .child("info")
                                    .getValue(genericTypeIndicator);
                            newPPE.PPEList = values.toArray(new String[values.size()]);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Query userQuery = userReference.orderByChild("uuid").equalTo(newPPE.id);
                userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            newPPE.name = (String) data.child("firstName").getValue();
                            newPPE.email = (String) data.child("email").getValue();
                            newPPE.phone = (String) data.child("phone").getValue();
                            boolean isOrg = (Boolean) data.child("isOrganization").getValue();
                            if (isOrg) {
                                newPPE.organization = (String) data.child("organizationName").getValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                postsList.add(newPPE);
                MarkerOptions newMarker = new MarkerOptions()
                        .position(new LatLng(location.latitude, location.longitude))
                        .title(newPPE.id + " " + newPPE.type);
                mMap.addMarker(newMarker);
                mMap.setOnMarkerClickListener(mapClickListener);
                markerList.add(newMarker);
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
                System.out.println("All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("There was an error with this query: " + error);
            }
        });
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
                            Log.i("asdfa", "This is the last known location: " + mLastKnownLocation);
                        }
                    });
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        displayMyLocation();
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
