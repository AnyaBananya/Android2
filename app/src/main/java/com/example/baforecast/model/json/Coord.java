package com.example.baforecast.model.json;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class Coord {
    @SerializedName("lon")
    @Expose
    private float lat;
    @SerializedName("lat")
    @Expose
    private  float lon;

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }
}
