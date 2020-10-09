package com.example.guireglogin;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface JsonPlaceHolderAPI {
    @FormUrlEncoded
    @POST("core/register/")
    Call<User> createUserJson(@FieldMap Map<String, String> fields);
    @FormUrlEncoded
    @POST("login/")
    Call<User> loginUserJson(@FieldMap Map <String, String> fields);
}