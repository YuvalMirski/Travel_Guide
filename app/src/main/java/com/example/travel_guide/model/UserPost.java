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
    String id = "";

    String userId = "";
    String name = "";
    String location = "";
    String about = "";
    String category = "";
    String postImgUrl = "";
    String isDeleted = "false";

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    long updateDate = new Long(0);

    //ImageView userProfile;
    final public static String COLLECTION_NAME = "UserPost";
    final public static String LAST_UPDATE = "PostsLastUpdateDate";

    public UserPost() { // this C'tor for ROOM

    }

    public UserPost(String name, String location, String about, String category, String userId) {
        this.name = name;
        this.location = location;
        this.about = about;
        this.userId = userId;
        this.category = category;
    }

    public static UserPost create(Map<String, Object> json) {
        String name = (String) json.get("name");
        String location = (String) json.get("location");
        String about = (String) json.get("about");
        String category = (String) json.get("category");
        String userId = (String) json.get("userId");
        String postImgUrl = (String) json.get("postImgUrl");
        String delete = (String) json.get("isDeleted");
        //Timestamp ts = (Timestamp)json.get("updateDate");
        UserPost userPost = new UserPost(name, location, about, category, userId);
        userPost.setPostImgUrl(postImgUrl);
        //convert from Time to long

        //TODO:: need to check why it is not working here - the program crash because its 0 and not json of Timestamp        System.out.println("print Json");
        System.out.println(json.get("updateDate"));
//            Timestamp ts = (Timestamp)json.get("updateDate");
//            Long updateDate = ts.getSeconds();
//            userPost.setUpdateDate(updateDate);
        userPost.setIsDeleted(delete);

        // userPost.setId(docId);
        return userPost;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("name", name);
        json.put("location", location);
        json.put("about", about);
        json.put("id", id);
        json.put("category", category);
        json.put("userId", userId);
        json.put("updateDate", FieldValue.serverTimestamp());// get time stamp from firebase
        json.put("postImgUrl", postImgUrl);
        json.put("isDeleted", isDeleted);
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return category;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public String getPostImgUrl() {
        return postImgUrl;
    }

    public void setPostImgUrl(String postImg) {
        this.postImgUrl = postImg;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getIsDeleted() {
        return isDeleted;
    }
}
