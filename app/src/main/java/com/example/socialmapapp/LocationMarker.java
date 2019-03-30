package com.example.socialmapapp;

import com.google.android.gms.maps.model.LatLng;

public class LocationMarker {
    public double latitude;
    public double longitude;
    public String title;
    public String description;
    public String dbKey;

    public LocationMarker(double latitude, double longitude, String title, String description, String dbKey) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
        this.dbKey = dbKey;
    }

    public LocationMarker() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
