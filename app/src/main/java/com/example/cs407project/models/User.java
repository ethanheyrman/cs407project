package com.example.cs407project.models;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String email;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String age;
    public String bio;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String firstName, String lastName, String phoneNumber, String age, String bio) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.bio = bio;
    }

}
