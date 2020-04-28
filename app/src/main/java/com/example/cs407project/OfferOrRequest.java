package com.example.cs407project;

import com.google.gson.Gson;

public class OfferOrRequest {
   // public int which;
    public String[] entries;
    public String ltd;
    public String longt;

    /*
    public String clothMask;
    public String surgicalMask;
    public String disposableRespirator;
    public String halfMask;
    public String fullMask;
    public String filters;
    public String goggles;
    public String faceShield;
    public String surgicalGown;


    public OfferOrRequest(int which, String clothMask, String surgicalMask, String disposableRespirator, String halfMask, String fullMask,
                          String filters, String goggles, String faceShield, String surgicalGown) {
        this.which = which;
        this.clothMask = clothMask;
        this.surgicalMask = surgicalMask;
        this.disposableRespirator = disposableRespirator;
        this.halfMask = halfMask;
        this.fullMask = fullMask;
        this.filters = filters;
        this.goggles = goggles;
        this.faceShield = faceShield;
        this.surgicalGown = surgicalGown;

    }


     */
    public OfferOrRequest(String[] entries, String ltd, String longt) {
   //     this.which = type;
        this.entries = entries;
        this.ltd = ltd;
        this.longt= longt;
        /*
        this.clothMask = entries[0];
        this.surgicalMask = entries[1];
        this.disposableRespirator = entries[2];
        this.halfMask = entries[3];
        this.fullMask = entries[4];
        this.filters = entries[5];
        this.goggles = entries[6];
        this.faceShield = entries[7];
        this.surgicalGown = entries[8];

         */
    }
}