package com.claudiolabarbera.missions;

import java.util.ArrayList;

/**
 * Created by labar on 19/07/2016.
 */
public class Mission {
    private int id;
    private String title;
    private String place;
    private int status;
    private String lat;
    private String lng;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;

    public Mission(int id, String title, String place, int status, String lat, String lng, String username) {
        this.id = id;
        this.title = title;
        this.place = place;
        this.status = status;
        this.lat = lat;
        this.lng = lng;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
