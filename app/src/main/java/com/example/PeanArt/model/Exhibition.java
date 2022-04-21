package com.example.PeanArt.model;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.Date;

public class Exhibition implements Serializable {
    private String id, title, detail, info, URI, UID, category, startdate, enddate, location;
    private int kind;
    // private Date startDate, endDate;
    // private GeoPoint location;
    private boolean liked;
    public Exhibition() {
    }

    public Exhibition(String id, String title, String detail, String info, String startdate, String enddate, String location, String URI, String UID, String category, int kind) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.info = info;
        this.startdate = startdate;
        this.enddate = enddate;
        this.location = location;
        this.URI = URI;
        this.UID = UID;
        this.category = category;
        this.kind = kind;
        this.liked = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStartDate() {
        return startdate;
    }

    public void setStartDate(String startdate) {
        this.startdate = startdate;
    }

    public String getEndDate() {
        return enddate;
    }

    public void setEndDate(String endDate) {
        this.enddate = enddate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
