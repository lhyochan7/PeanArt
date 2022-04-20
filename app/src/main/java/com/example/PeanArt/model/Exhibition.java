package com.example.PeanArt.model;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.Date;

public class Exhibition implements Serializable {
    private String id, title, detail, info, URI, UID, category;
    private int kind;
    private Date startdate, enddate;
    private GeoPoint location;

    public Exhibition() {
    }

    public Exhibition(String id, String title, String detail, String info, Date startDate, Date endDate, GeoPoint location, String URI, String UID, String category, int kind) {
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

    public Date getStartDate() {
        return startdate;
    }

    public void setStartDate(Date startDate) {
        this.startdate = startDate;
    }

    public Date getEndDate() {
        return enddate;
    }

    public void setEndDate(Date endDate) {
        this.enddate = endDate;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
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
}
