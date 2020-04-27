package com.example.cs407project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.example.cs407project.models.PPEPost;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
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

    ArrayList<PPEPost> displayPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Firebase Realtime Database and Auth references
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        usersReference = FirebaseDatabase.getInstance().getReference().child("users");
        Query userQuery = usersReference.orderByChild("uuid").equalTo(currentUser.getUid());
        // Layout References
        firstName = findViewById(R.id.first_name_container).findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name_container).findViewById(R.id.last_name);
        age = findViewById(R.id.age_container).findViewById(R.id.age);
        occupation = findViewById(R.id.occupation_container).findViewById(R.id.occupation);
        organizationName = findViewById(R.id.organization_name_container)
                .findViewById(R.id.organization_name);
        organizationNameContainer = findViewById(R.id.organization_name_container);
        listView = findViewById(R.id.post_list);

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                displayPosts = new ArrayList<>();

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

                    DataSnapshot postsSnapshot = data.child("/posts");
                    Iterable<DataSnapshot> posts = postsSnapshot.getChildren();
                    for (DataSnapshot post : posts) {
                        Long id = (Long) post.child("id").getValue();
                        String type = (String) post.child("type").getValue();
                        java.util.HashMap<String, String> ppeList = (java.util.HashMap<String, String>) post.child("ppe_list").getValue();
//
                        Log.i("PPE_LEN", (ppeList.get("gloves").toString()));
//                        DataSnapshot ppeSnapshot = data.child("/ppe_list");
//                        Iterable<DataSnapshot> ppeIterable = ppeSnapshot.getChildren();
//                        for (DataSnapshot ppeType : ppeIterable) {
//                            ppeList.add(ppeType.toString());
//                        }
//                        displayPosts.add(ppePost);

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displayPosts);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Intent intent = new Intent(getApplicationContext(), PPEPostActivity.class);
////                intent.putExtra("noteid", position);
////                startActivity(intent);
//            }
//        });
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
