package com.example.PeanArt.model;

import java.util.ArrayList;

public class User {
    String ID, nickname;
    ArrayList<String> liked, follow;

    public User() {
    }
    public User(String ID, String nickname, ArrayList<String> liked, ArrayList<String> follow) {
        this.ID = ID;
        this.nickname = nickname;
        this.liked = liked;
        this.follow = follow;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ArrayList<String> getLiked() {
        return liked;
    }

    public void setLiked(ArrayList<String> liked) {
        this.liked = liked;
    }

    public ArrayList<String> getFollow() {
        return follow;
    }

    public void setFollow(ArrayList<String> follow) {
        this.follow = follow;
    }
}
