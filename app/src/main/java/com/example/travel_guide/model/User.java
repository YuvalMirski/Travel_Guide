package com.example.travel_guide.model;

import java.util.List;

public class User {

    String name, email, sex, country, userName, password, id;
    boolean isChecked;
    //List<UserPost>userActivities; // list of posts\activities of user
    //List<UserPost>savedActivities;
    //TODO - personal picture


    public User(String name, String email, String sex, String country, String userName, String password, String id, boolean isChecked) {
        this.name = name;
        this.email = email;
        this.sex = sex;
        this.country = country;
        this.userName = userName;
        this.password = password;
        this.id = id;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
