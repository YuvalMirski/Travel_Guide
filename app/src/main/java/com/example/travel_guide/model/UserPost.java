package com.example.travel_guide.model;


//this class store the data of a post\ activity

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class UserPost {
    @PrimaryKey
    @NonNull
    String id;
    boolean isChecked; // for deleting data from firebase
    String name,location, about, category;

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    Long updateDate = new Long(0);

    //ImageView userProfile;
    final public static String COLLECTION_NAME = "UserPost";

    public UserPost() { // this C'tor for ROOM
        this.isChecked = false;
    }

    public UserPost(String name, String location, String about, String id, String category) {
        this.name = name;
        this.location = location;
        this.about = about;
        this.id = id;
        this.category = category;
    }

    public static UserPost create(Map<String, Object> json) {
        String name = (String)json.get("name");
        String location = (String)json.get("location");
        String about = (String)json.get("about");
        String id = (String)json.get("id");
        String category = (String)json.get("category");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();
        UserPost userPost = new UserPost(name,location,about,id,category);
        userPost.setUpdateDate(updateDate);
        return  userPost;
    }

    public Map<String, Object> toJson() {
      Map<String,Object>json = new HashMap<String,Object>();
        json.put("name",name);
        json.put("location",location);
        json.put("about",about);
        json.put("id",id);
        json.put("category",category);
        json.put("updateDate", FieldValue.serverTimestamp());// get time stamp from server
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

//    public ImageView getUserProfile() {
//        return userProfile;
//    }
//
//    public void setUserProfile(ImageView userProfile) {
//        this.userProfile = userProfile;
//    }

    public String getType() {
        return category;
    }

    public long getUpdateData() {
        return updateDate;
    }
}
