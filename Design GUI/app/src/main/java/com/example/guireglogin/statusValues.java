package com.example.guireglogin;

import com.google.gson.annotations.SerializedName;

public class statusValues {

    @SerializedName("contact")
    public boolean contact;

    @SerializedName("identifier")
    public String identifier;

    @SerializedName("unconfirmed_contact")
    public boolean unconfirmed_contact;

    @SerializedName("count_confirmed")
    public String count_confirmed;

    @SerializedName("count_unconfirmed")
    public String count_unconfirmed;

    @SerializedName("total_interactions")
    public String total_interactions;

}