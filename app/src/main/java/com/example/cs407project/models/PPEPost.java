package com.example.cs407project.models;
import java.util.HashMap;

public class PPEPost {
    public String id;
    public String authorUUID;
    public String type;
    public String lat;
    public String lon;
    public HashMap<String, String> PPEList;

    public PPEPost () {}

    public PPEPost(String id, String authorUUID, String type, HashMap<String, String> PPEList, String lat, String lon) {
        this.id = id;
        this.authorUUID = authorUUID;
        this.type = type;
        this.PPEList = PPEList;
        this.lat = lat;
        this.lon = lon;
    }
}
