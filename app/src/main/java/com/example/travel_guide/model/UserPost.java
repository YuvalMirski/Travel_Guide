package com.example.travel_guide.model;


//this class store the data of a post\ activity

import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

public class UserPost {

    String name,location, type, about, id, category;
    ImageView userProfile;
    final public static String COLLECTION_NAME = "UserPost";
    public UserPost(String name, String location, String type, String about, String id, String category) {
        this.name = name;
        this.location = location;
        this.type = type;
        this.about = about;
        this.id = id;
        this.category = category;
    }

    public static UserPost create(Map<String, Object> json) {
        String name = (String)json.get("name");
        String location = (String)json.get("location");
        String type = (String)json.get("type");
        String about = (String)json.get("about");
        String id = (String)json.get("id");
        String category = (String)json.get("category");
        UserPost userPost = new UserPost(name,location,type,about,id,category);

        return  userPost;
    }

    public Map<String, Object> toJson() {
      Map<String,Object>json = new HashMap<String,Object>();
        json.put("name",name);
        json.put("location",location);
        json.put("type",type);
        json.put("about",about);
        json.put("id",id);
        json.put("category",category);
        return json;

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
