package com.example.testapi;

import com.google.gson.annotations.SerializedName;

public class User {

    private String password;

    private String email;

    public User(String email, String password) {
       this.email=email;
       this.password=password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
