package com.example.cs407project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    public EditText firstName;
    public EditText lastName;
    public EditText occupation;
    public EditText age;
    public EditText organizationName;
    public View organizationNameContainer;
    public ListView listView;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference usersReference;
    DatabaseReference postsReference;

    ArrayList<String> displayPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        final Context context = getApplicationContext();
        // Firebase Realtime Database and Auth references
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        usersReference = FirebaseDatabase.getInstance().getReference().child("users");
        postsReference = FirebaseDatabase.getInstance().getReference().child("posts");
        Query userQuery = usersReference.orderByChild("uuid").equalTo(currentUser.getUid());
        Query postsQuery = postsReference.orderByChild("authorUUID").equalTo(currentUser.getUid());

        // Profile Layout References
        firstName = findViewById(R.id.first_name_container).findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name_container).findViewById(R.id.last_name);
        age = findViewById(R.id.age_container).findViewById(R.id.age);
        occupation = findViewById(R.id.occupation_container).findViewById(R.id.occupation);
        organizationName = findViewById(R.id.organization_name_container)
                .findViewById(R.id.organization_name);
        organizationNameContainer = findViewById(R.id.organization_name_container);
        listView = (ListView) findViewById(R.id.listView);

        // Persistent Layout References
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        displayPosts = new ArrayList<String>();
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot data: dataSnapshot.getChildren()){
                    firstName.setText((String) data.child("firstName").getValue());
                    lastName.setText((String) data.child("lastName").getValue());
                    age.setText((String) data.child("age").getValue());
                    occupation.setText((String) data.child("occupation").getValue());
                    Boolean isOrganization = (Boolean) data.child("isOrganization").getValue();

                    if (isOrganization) {
                        organizationNameContainer.setVisibility(View.VISIBLE);
                        organizationName.setText((String) data.child("organizationName").getValue());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        postsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    String id = (String) data.child("id").getValue();
                    String type = (String) data.child("type").getValue();
                    java.util.HashMap<String, String> ppeList = (java.util.HashMap<String, String>) data.child("PPEList").getValue();
                    String lat = (String) data.child("lat").getValue();
                    String lon = (String) data.child("lon").getValue();
                    Log.i("data snapshot size", id);
                    displayPosts.add(String.format("id: %s\nlat: %s lon: %s", id, lat, lon));

                }
                ArrayAdapter adapter = new ArrayAdapter(context, R.layout.list_item_layout, displayPosts);
                listView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_search:
                    Intent searchIntent = new Intent(ProfileActivity.this, SearchActivity.class);
                    startActivity(searchIntent);
                    return true;
                case R.id.action_profile:
                    Intent profileIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                    return true;
                case R.id.action_navigation:
                    Intent navigationIntent = new Intent(ProfileActivity.this, NavigationActivity.class);
                    startActivity(navigationIntent);
                    return true;
                case R.id.action_add:
                    Intent addIntent = new Intent(ProfileActivity.this, AddActivity.class);
                    startActivity(addIntent);
                    return true;
                case R.id.action_settings:
                    Intent settingsIntent = new Intent(ProfileActivity.this, SettingsActivity.class);
                    startActivity(settingsIntent);
                    return true;
                default:
                    return false;
            }
            }

        };
}
