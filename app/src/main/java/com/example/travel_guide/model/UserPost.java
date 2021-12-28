package com.example.travel_guide.model;


//this class store the data of a post\ activity

import android.widget.ImageView;

public class UserPost {

    String name,location, type, about, id, category;
    ImageView userProfile;

    public UserPost(String name, String location, String type, String about, String id, String category) {
        this.name = name;
        this.location = location;
        this.type = type;
        this.about = about;
        this.id = id;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ImageView getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(ImageView userProfile) {
        this.userProfile = userProfile;
    }
}
