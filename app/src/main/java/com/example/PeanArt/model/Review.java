package com.example.PeanArt.model;

import com.google.firebase.Timestamp;

public class Review {
    String writerID, exhibitionID, content;
    Timestamp writeDate;

    public Review() {
    }

    public Review(String writerID, String exhibitionID, String content, Timestamp writeDate) {
        this.writerID = writerID;
        this.exhibitionID = exhibitionID;
        this.content = content;
        this.writeDate = writeDate;
    }

    public String getWriterID() {
        return writerID;
    }

    public void setWriterID(String writerID) {
        this.writerID = writerID;
    }

    public String getExhibitionID() {
        return exhibitionID;
    }

    public void setExhibitionID(String exhibitionID) {
        this.exhibitionID = exhibitionID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(Timestamp writeDate) {
        this.writeDate = writeDate;
    }
}
