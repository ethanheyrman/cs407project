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
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    public EditText firstName;
    public EditText lastName;
    public EditText email;
    public EditText phone;
    public EditText organizationName;
    public View organizationNameContainer;
    public ListView listView;
    public TextView activePosts;
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
        email = findViewById(R.id.email_container).findViewById(R.id.email);
        phone = findViewById(R.id.phone_container).findViewById(R.id.phone);
        organizationNameContainer = findViewById(R.id.organization_name_container);
        organizationName = organizationNameContainer.findViewById(R.id.organization_name);
        listView = (ListView) findViewById(R.id.listView);
        activePosts = findViewById(R.id.active_posts);
//        listView.setEmptyView((TextView) findViewById(R.id.listView).findViewById(R.id.emptyElement));
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
                    email.setText((String) data.child("email").getValue());
                    phone.setText((String) data.child("phone").getValue());
                    Boolean isOrganization = (Boolean) data.child("isOrganization").getValue();

                    if (isOrganization) {
                        organizationNameContainer.setVisibility(View.VISIBLE);
                        organizationName.setText((String) data.child("organizationName").getValue());

                        activePosts.setTop(R.id.email_container);
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
            item -> {
            switch (item.getItemId()) {
                case R.id.action_search:
                    Intent searchIntent = new Intent(ProfileActivity.this, SearchActivity.class);
                    startActivity(searchIntent);
                    return true;
                case R.id.action_profile:
                    Intent profileIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                    return true;
                case R.id.action_home:
                    Intent navigationIntent = new Intent(ProfileActivity.this, HomeActivity.class);
                    startActivity(navigationIntent);
                    return true;
                case R.id.action_add:
                    Intent addIntent = new Intent(ProfileActivity.this, AddActivity.class);
                    startActivity(addIntent);
                    return true;
                case R.id.action_resources:
                    Intent settingsIntent = new Intent(ProfileActivity.this, ResourcesActivity.class);
                    startActivity(settingsIntent);
                    return true;
                default:
                    return false;
            }
            };
}
