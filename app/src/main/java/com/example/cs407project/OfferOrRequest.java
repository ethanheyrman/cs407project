package com.example.cs407project;

public class OfferOrRequest {
    public String which;
    public int clothMask;
    public int surgicalMask;
    public int disposableRespirator;
    public int halfMask;
    public int fullMask;
    public int filters;
    public int goggles;
    public int surgicalGown;
    public int faceShield;

    public OfferOrRequest(String which, int clothMask, int surgicalMask, int disposableRespirator, int halfMask, int fullMask,
                          int filters, int goggles, int surgicalGown, int faceShield)
    {
        this.which = which;
        this.clothMask = clothMask;
        this.surgicalMask = surgicalMask;
        this.disposableRespirator = disposableRespirator;
        this.halfMask = halfMask;
        this.fullMask = fullMask;
        this.filters = filters;
        this.goggles = goggles;
        this.surgicalGown = surgicalGown;
        this.faceShield = faceShield;
    }
}
