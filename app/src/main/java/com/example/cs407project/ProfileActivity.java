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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cs407project.models.ProfilePPEPost;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {
    public EditText firstName;
    public EditText lastName;
    public EditText email;
    public EditText phone;
    public EditText organizationName;
    public View organizationNameContainer;
    public ListView listView;
    public TextView activePosts;
    public SwitchMaterial editProfile;
    public boolean editing = false;
    public TextView welcome;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference usersReference;
    DatabaseReference postsReference;

    ArrayList<ProfilePPEPost> displayPosts;

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
        Query postQuery = postsReference.orderByKey().startAt(currentUser.getUid());

        // Profile Layout References
        firstName = findViewById(R.id.first_name_container).findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name_container).findViewById(R.id.last_name);
        email = findViewById(R.id.email_container).findViewById(R.id.email);
        phone = findViewById(R.id.phone_container).findViewById(R.id.phone);
        organizationNameContainer = findViewById(R.id.organization_name_container);
        organizationName = organizationNameContainer.findViewById(R.id.organization_name);
        listView = (ListView) findViewById(R.id.listView);
        activePosts = findViewById(R.id.active_posts);
        editProfile = findViewById(R.id.edit_profile_switch);
        // Persistent Layout References
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        displayPosts = new ArrayList<>();
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
                //findViewById(R.id.welcome).setText("Hello, " + firstName.getText().toString() + "!");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        postQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    if (activePosts.getVisibility() == View.INVISIBLE) activePosts.setVisibility(View.VISIBLE);
                    String type = "";
                    if (Objects.requireNonNull(data.getKey()).endsWith("request"))
                        type = "Request";
                    else if (Objects.requireNonNull(data.getKey()).endsWith("offer")) {
                        Log.i("TAG", data.getKey());
                        type = "Offer";
                    }
                    ArrayList ppe = (ArrayList) data.child("info").getValue();
                    displayPosts.add(new ProfilePPEPost(data.getKey(), type, ppe));
                }
                ProfilePPEAdapter adapter = new ProfilePPEAdapter(context, displayPosts);
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

        editProfile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                firstName.setEnabled(!firstName.isEnabled());
                lastName.setEnabled(!lastName.isEnabled());
                organizationName.setEnabled(!organizationName.isEnabled());
                if (validateForm()) {
                        usersReference.child(currentUser.getUid()).child("firstName").setValue(firstName.getText().toString());
                        usersReference.child(currentUser.getUid()).child("lastName").setValue(lastName.getText().toString());
                        usersReference.child(currentUser.getUid()).child("organizationName").setValue(organizationName.getText().toString());
                    }
                Log.i("TAG", String.valueOf(isChecked));
            }
        });
    }

    private Boolean validateForm() {
        boolean error = false;
        if (firstName.getText().toString().length() <= 0) {
            firstName.setError("First name must be at least 1 character long.");
            error = true;
        }
        if (lastName.getText().toString().length() <= 0) {
            lastName.setError("Last name must be at least 1 character long.");
            error = true;
        }
        if (organizationNameContainer.getVisibility() == View.VISIBLE) {
            if (organizationName.getText().toString().length() <= 0) {
                organizationName.setError("Organization name must be at least 1 character long.");
                error = true;
            }
        }
        return !error;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            item -> {
            switch (item.getItemId()) {
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
