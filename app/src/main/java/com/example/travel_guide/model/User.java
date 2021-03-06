package com.example.travel_guide.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    String userName, email, sex, country, id, avatarUrl;//, password
    List<String> lstSaved; // TODO:: id of all the post of the saved post
    List<String> lstUserPosts;
    final public static String COLLECTION_NAME = "Users";

    public User(String userName, String email, String sex, String country, List<String> lstSaved, List<String> lstUserPosts) {
        this.userName = userName;
        this.email = email;
        this.sex = sex;
        this.country = country;
        this.avatarUrl = null;
        this.lstSaved = lstSaved;
        this.lstUserPosts = lstUserPosts;
    }

    public static User create(Map<String, Object> json) {
        String userName = (String) json.get("userName");
        String email = (String) json.get("email");
        String sex = (String) json.get("sex");
        String country = (String) json.get("country");
        String url = (String) json.get("avatarUrl");
        List<String> lstSaved = (List<String>) json.get("lstSaved");
        List<String> lstUserPosts = (List<String>) json.get("lstUserPosts");
        User user = new User(userName, email, sex, country, lstSaved, lstUserPosts);
        user.setAvatarUrl(url);
        return user;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("userName", userName);
        json.put("email", email);
        json.put("sex", sex);
        json.put("country", country);
        json.put("id", id);
        json.put("avatarUrl", avatarUrl);
        json.put("lstSaved", lstSaved);
        json.put("lstUserPosts", lstUserPosts);
        return json;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUserName() {
        return userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getLstSaved() {
        return lstSaved;
    }

    public void setLstSaved(List<String> lstSaved) {
        this.lstSaved = lstSaved;
    }

    public List<String> getLstUserPosts() {
        return lstUserPosts;
    }

    public void setLstUserPosts(List<String> lstUserPosts) {
        this.lstUserPosts = lstUserPosts;
    }

    public void setAvatarUrl(String url) {
        this.avatarUrl = url;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
