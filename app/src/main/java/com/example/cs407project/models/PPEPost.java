package com.example.cs407project.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PPEPost {
    public String id;
    public String type;
    public ArrayList<String> PPEList;

    public PPEPost () {}

    public PPEPost(String id, String type, ArrayList<String> PPEList) {
        this.id = id;
        this.type = type;
        this.PPEList = PPEList;
    }
}
