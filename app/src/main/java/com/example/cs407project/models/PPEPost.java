package com.example.cs407project.models;
import com.firebase.geofire.GeoLocation;

public class PPEPost {
    public String id;
    public String name;
    public String organization;
    public String email;
    public String authorUUID;
    public String phone;
    public String type;
    public GeoLocation location;
    public String[] PPEList;

    public PPEPost () {}

}


//public class PostWithInfo {
//
//    public String[] PPEList;
//    public GeoLocation location;
//    public String name;
//    public String email;
//    public String phone;
//    public String organization;
//
//    PostWithInfo()
//    {
//
//    }
//}
