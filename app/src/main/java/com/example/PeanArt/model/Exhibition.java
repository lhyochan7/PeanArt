package com.example.PeanArt.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class Exhibition {
    private String title, detail, info;
    private Date startDate, endDate;
    private GeoPoint location;

    public Exhibition() {
    }

    public Exhibition(String title, String detail, String info, Date startDate, Date endDate, GeoPoint location) {
        this.title = title;
        this.detail = detail;
        this.info = info;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
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
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
