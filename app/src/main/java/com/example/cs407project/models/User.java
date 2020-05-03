package com.example.cs407project.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String uuid;
    public String email;
    public String phone;
    public String firstName;
    public String lastName;
    public Boolean isOrganization = false;
    public String organizationName = null;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uuid, String email, String phone, String firstName, String lastName,
                Boolean isOrganization, String organizationName) {
        this.uuid = uuid;
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isOrganization = isOrganization;
        this.organizationName = organizationName;
    }


}
