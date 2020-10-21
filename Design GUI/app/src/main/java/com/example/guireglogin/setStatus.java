package com.example.guireglogin;

import com.google.gson.annotations.SerializedName;

public class setStatus {

    @SerializedName("infected")
    private boolean infected;

    @SerializedName("interactions")
    public Interactions[] interactions;
}
