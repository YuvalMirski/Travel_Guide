package com.example.travel_guide.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    String userName, email, sex, country, password, id;
    final public static String COLLECTION_NAME = "Users";

    public User(String userName, String email, String sex, String country, String password, String id) {
        this.userName = userName;
        this.email = email;
        this.sex = sex;
        this.country = country;
        this.password = password;
        this.id = id;
    }

    public static User create(Map<String, Object> json) {
        String userName = (String)json.get("userName");
        String email = (String)json.get("email");
        String sex = (String)json.get("sex");
        String country = (String)json.get("country");
        String password = (String)json.get("password");
        String id = (String)json.get("id");

        User user = new User(userName,email,sex,country,password,id);

        return user;
    }

    public Map<String, Object> toJson() {
        Map<String,Object>json = new HashMap<String,Object>();
        json.put("userName",userName);
        json.put("email",email);
        json.put("sex",sex);
        json.put("country",country);
        json.put("password",password);
        json.put("id",id);
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

}
