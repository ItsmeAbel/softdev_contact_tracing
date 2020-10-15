package com.example.guireglogin;

import com.google.gson.annotations.SerializedName;

public class User {

    private String password;

    private String email;

    public User(String email, String password) {
        this.email=email;
        this.password=password;
    }
    @SerializedName("token")
    public String token;

}