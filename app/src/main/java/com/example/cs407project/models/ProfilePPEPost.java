package com.example.cs407project.models;

import java.util.ArrayList;

public class ProfilePPEPost {


    public String id;
    public String type;
    public String[] ppe;

    public ProfilePPEPost() {}

    public ProfilePPEPost(String id, String type, ArrayList ppe) {
        this.id = id;
        this.type = type;
        this.ppe = new String[] {(String) ppe.get(0), (String) ppe.get(1), (String) ppe.get(2),
                (String) ppe.get(3), (String) ppe.get(4), (String) ppe.get(5), (String) ppe.get(6),
                (String) ppe.get(7), (String) ppe.get(8)};
    }
}
