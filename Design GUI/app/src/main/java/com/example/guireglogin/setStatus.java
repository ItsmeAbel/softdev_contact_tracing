package com.example.guireglogin;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class setStatus {

    @SerializedName("infected")
    private boolean infected;

    @SerializedName("interactions")
    public ArrayList<Interactions> interactions;
}
