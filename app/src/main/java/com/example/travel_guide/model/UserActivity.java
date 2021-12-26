package com.example.travel_guide.model;


//this class store the data of a post\ activity

public class UserActivity {

    String name,location, type, about, id;

    public UserActivity(String name, String location, String type, String about, String id) {
        this.name = name;
        this.location = location;
        this.type = type;
        this.about = about;
        this.id = id;
    }
}
