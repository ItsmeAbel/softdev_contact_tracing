package com.example.guireglogin;

public class FriendClass {
    private String friendname;
    private String friendnumber;

    public FriendClass(String name, String number){
        friendname = name;
        friendnumber = number;
    }

    public String getName(){
        return friendname;
    }

    public String getNumber(){
        return friendnumber;
    }

}
