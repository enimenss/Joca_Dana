package com.example.a38162.attractionsofnis;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    String name;
    String surname;
    String username;
    String password;
    String phone_number;
    String picture;
    String score;

@Exclude
    String userId;
    public User() {}

    public User(String userId, String name, String surname, String username, String password, String phone_number, String picture, String score) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.phone_number = phone_number;
        this.picture = picture;
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getPicture() {
        return picture;
    }

    public  String getScore() { return  score; }
}
