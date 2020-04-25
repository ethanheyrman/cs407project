package com.example.cs407project.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String uuid;
    private String email;
    private String firstName;
    private String lastName;
    private String age;
    private String occupation;
    private Boolean isOrganization = false;
    private String organizationName = null;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uuid, String email, String firstName, String lastName, String occupation,
                String age, Boolean isOrganization, String organizationName) {
        this.uuid = uuid;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.occupation = occupation;
        this.age = age;
        this.isOrganization = isOrganization;
        this.organizationName = organizationName;
    }

}
