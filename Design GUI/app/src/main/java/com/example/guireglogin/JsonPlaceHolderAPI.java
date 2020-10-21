package com.example.guireglogin;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface JsonPlaceHolderAPI {
    @FormUrlEncoded
    @POST("core/register/")
    Call<User> createUserJson(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("login/")
    Call<User> loginUserJson(@FieldMap Map <String, String> fields);

    @GET("core/status/")
    Call<statusValues> getStatus(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("core/status/")
    Call<setStatus> setSickStatus(@Header("Authorization") String token, @FieldMap Map<String, Boolean> fields);

    @FormUrlEncoded
    @POST("core/status/")
    Call<setStatus> pushInteractions(@Header("Authorization") String token, @Field("interactions") ArrayList<String> interactionslist);

}