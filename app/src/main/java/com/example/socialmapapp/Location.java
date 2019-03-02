package com.example.socialmapapp;

import com.google.android.gms.maps.model.LatLng;

public class Location {
    public LatLng current;

    public Location() {
    }

    public Location (LatLng current){
        this.current = current;
    }
}
